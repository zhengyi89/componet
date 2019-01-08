package com.zbjdl.common.lock;

import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisLock {

	private static final Lock NO_LOCK = new Lock(new UUID(0l, 0l), 0l);

	private static final int ONE_SECOND = 1000;

	//缺省锁过期时间
	public static final int DEFAULT_EXPIRY_TIME_MILLIS = Integer.getInteger("com.hengbao.common.lock.expiry.millis",
			60 * ONE_SECOND);
	//缺省的获取锁超时时间
	public static final int DEFAULT_ACQUIRE_TIMEOUT_MILLIS = Integer.getInteger("com.hengbao.common.lock.acquiry.millis",
			10 * ONE_SECOND);
	//获取锁失败后的重试、等待时间
	public static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = Integer
			.getInteger("com.hengbao.common.lock.acquiry.resolution.millis", 100);
	
	public static final String KEY_PREFIX = "LOCK_KEY_";

	private final StringRedisTemplate redisTemplate;

	private final String lockKeyPath;

	private final int lockExpiryInMillis;
	private final int acquiryTimeoutInMillis;
	private final UUID lockUUID;

	private Lock lock = null;

	protected static class Lock {
		private UUID uuid;
		private long expiryTime;

		protected Lock(UUID uuid, long expiryTimeInMillis) {
			this.uuid = uuid;
			this.expiryTime = expiryTimeInMillis;
		}

		protected static Lock fromString(String text) {
			try {
				String[] parts = text.split(":");
				UUID theUUID = UUID.fromString(parts[0]);
				long theTime = Long.parseLong(parts[1]);
				return new Lock(theUUID, theTime);
			} catch (Exception any) {
				return NO_LOCK;
			}
		}

		public UUID getUUID() {
			return uuid;
		}

		public long getExpiryTime() {
			return expiryTime;
		}

		@Override
		public String toString() {
			return uuid.toString() + ":" + expiryTime;
		}

		boolean isExpired() {
			return getExpiryTime() < System.currentTimeMillis();
		}

		boolean isExpiredOrMine(UUID otherUUID) {
			return this.isExpired() || this.getUUID().equals(otherUUID);
		}
	}

	/**
	 * Detailed constructor with default acquire timeout 10000 msecs and lock
	 * expiration of 60000 msecs.
	 * 
	 * @param redisTemplate
	 * @param lockKey
	 *            lock key (ex. account:1, ...)
	 */
	public RedisLock(StringRedisTemplate redisTemplate, String lockKey) {
		this(redisTemplate, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
	}

	/**
	 * Detailed constructor with default lock expiration of 60000 msecs.
	 * 
	 * @param redisTemplate
	 * @param lockKey
	 *            lock key (ex. account:1, ...)
	 * @param acquireTimeoutMillis
	 *            acquire timeout in miliseconds (default: 10000 msecs)
	 */
	public RedisLock(StringRedisTemplate redisTemplate, String lockKey, int acquireTimeoutMillis) {
		this(redisTemplate, lockKey, acquireTimeoutMillis, DEFAULT_EXPIRY_TIME_MILLIS);
	}

	/**
	 * Detailed constructor.
	 * 
	 * @param redisTemplate
	 * @param lockKey
	 *            lock key (ex. account:1, ...)
	 * @param acquireTimeoutMillis
	 *            acquire timeout in miliseconds (default: 10000 msecs)
	 * @param expiryTimeMillis
	 *            lock expiration in miliseconds (default: 60000 msecs)
	 */
	public RedisLock(StringRedisTemplate redisTemplate, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
		this(redisTemplate, lockKey, acquireTimeoutMillis, expiryTimeMillis, UUID.randomUUID());
	}

	/**
	 * Detailed constructor.
	 * 
	 * @param redisTemplate
	 * @param lockKey
	 *            lock key (ex. account:1, ...)
	 * @param acquireTimeoutMillis
	 *            acquire timeout in miliseconds (default: 10000 msecs)
	 * @param expiryTimeMillis
	 *            lock expiration in miliseconds (default: 60000 msecs)
	 * @param uuid
	 *            unique identification of this lock
	 */
	public RedisLock(StringRedisTemplate redisTemplate, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis,
			UUID uuid) {
		this.redisTemplate = redisTemplate;
		this.lockKeyPath = KEY_PREFIX+lockKey;
		this.acquiryTimeoutInMillis = acquireTimeoutMillis;
		this.lockExpiryInMillis = expiryTimeMillis + 1;
		this.lockUUID = uuid;
		;
	}

	/**
	 * @return lock uuid
	 */
	public UUID getLockUUID() {
		return lockUUID;
	}

	/**
	 * @return lock key path
	 */
	public String getLockKeyPath() {
		return lockKeyPath;
	}

	/**
	 * Acquire lock.
	 * 
	 * @return true if lock is acquired, false acquire timeouted
	 * @throws InterruptedException
	 *             in case of thread interruption
	 */
	public synchronized boolean acquire() throws InterruptedException {
		return acquire(redisTemplate);
	}

	/**
	 * Acquire lock.
	 * 
	 * @param redisTemplate
	 * @return true if lock is acquired, false acquire timeouted
	 * @throws InterruptedException
	 *             in case of thread interruption
	 */
	protected synchronized boolean acquire(StringRedisTemplate redisTemplate) throws InterruptedException {
		int timeout = acquiryTimeoutInMillis;
		while (timeout >= 0) {

			final Lock newLock = asLock(System.currentTimeMillis() + lockExpiryInMillis);
//			if (redisTemplate.getConnectionFactory().getConnection().setNX(lockKeyPath.getBytes(),
//					newLock.toString().getBytes())) {
//				this.lock = newLock;
//				return true;
//			}
			//using RedisCallback,fetching and returning the connection.
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	            public Boolean doInRedis(RedisConnection connection)throws DataAccessException {  
	                return connection.setNX(lockKeyPath.getBytes(), newLock.toString().getBytes());  
	            }
	        });  
			if (result) {
				this.lock = newLock;
				return true;
			}		
			
			final String currentValueStr = redisTemplate.opsForValue().get(lockKeyPath);
			final Lock currentLock = Lock.fromString(currentValueStr);
			if (currentLock.isExpiredOrMine(lockUUID)) {
				String oldValueStr = redisTemplate.opsForValue().getAndSet(lockKeyPath, newLock.toString());
				if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
					this.lock = newLock;
					return true;
				}
			}

			timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
			Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);
		}

		return false;
	}

	/**
	 * Renew lock.
	 * 
	 * @return true if lock is acquired, false otherwise
	 * @throws InterruptedException
	 *             in case of thread interruption
	 */
	public boolean renew() throws InterruptedException {
		final Lock lock = Lock.fromString(redisTemplate.opsForValue().get(lockKeyPath));
		if (!lock.isExpiredOrMine(lockUUID)) {
			return false;
		}

		return acquire(redisTemplate);
	}

	/**
	 * Acquired lock release.
	 */
	public synchronized void release() {
		release(redisTemplate);
	}

	/**
	 * Acquired lock release.
	 * 
	 * @param redisTemplate
	 */
	protected synchronized void release(StringRedisTemplate redisTemplate) {
		if (isLocked()) {
			redisTemplate.delete(lockKeyPath);
			this.lock = null;
		}
	}

	/**
	 * Check if owns the lock
	 * 
	 * @return true if lock owned
	 */
	public synchronized boolean isLocked() {
		return this.lock != null;
	}

	/**
	 * Returns the expiry time of this lock
	 * 
	 * @return the expiry time in millis (or null if not locked)
	 */
	public synchronized long getLockExpiryTimeInMillis() {
		return this.lock.getExpiryTime();
	}

	private Lock asLock(long expires) {
		return new Lock(lockUUID, expires);
	}

}

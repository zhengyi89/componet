package com.zbjdl.common.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service("lockJobTemplate")
public class LockJobTemplate {

	@Autowired
	StringRedisTemplate redisTemplate;

	/**
	 * 执行一个需要分布锁的任务，会在规定时间内尝试多次获取锁，获取锁失败会抛出异常
	 * 
	 * @param key
	 * @param job
	 * @param args
	 * @return
	 */
	public Object doJob(String key, LockJob job, Object... args) {
		RedisLock lock = new RedisLock(redisTemplate, key);
		try {
			if (lock.acquire()) {
				return job.doJob(args);
			} else {
				throw new RuntimeException("can't get lock!");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("can't get lock!");
		} finally {
			lock.release();
		}
	}

	/**
	 * 执行一个需要分布锁的任务，会在规定时间内尝试多次获取锁，获取锁失败会抛出异常
	 * 
	 * @param key
	 * @param acquireTimeoutMillis
	 * 			获取锁超时时间，单位毫秒 (miliseconds) (default: 10000 msecs)
	 * @param job
	 * @param args
	 * @return
	 */
	public Object doJob(String key, int acquireTimeoutMillis, LockJob job, Object... args) {
		RedisLock lock = new RedisLock(redisTemplate, key, acquireTimeoutMillis);
		try {
			if (lock.acquire()) {
				return job.doJob(args);
			} else {
				throw new RuntimeException("can't get lock!");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("can't get lock!");
		} finally {
			lock.release();
		}
	}

}

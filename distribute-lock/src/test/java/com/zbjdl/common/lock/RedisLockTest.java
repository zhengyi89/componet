package com.zbjdl.common.lock;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zbjdl.common.lock.RedisLock;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LockApplication.class)
public class RedisLockTest {


	@Autowired
	StringRedisTemplate redisTemplate;
	
    @Test
    public void testAcquire() throws InterruptedException {

        RedisLock lock = new RedisLock(redisTemplate, "testlock2");
        assertTrue(lock.acquire());

        RedisLock lock2 = new RedisLock(redisTemplate, "testlock2", 1000);
        assertFalse(lock2.acquire());

        lock.release();

        lock2 = new RedisLock(redisTemplate, "testlock2", 1000);
        assertTrue(lock2.acquire());
        lock2.release();
    }

    @Test
    public void testRenew() throws InterruptedException {

        RedisLock lock = new RedisLock(redisTemplate, "testlock2");
        assertTrue(lock.acquire());

        Thread.sleep(2000l);

        assertTrue(lock.renew());

        lock.release();

        RedisLock lock2 = new RedisLock(redisTemplate, "testlock2", 1000);
        assertTrue(lock2.acquire());
        lock2.release();
    }

    @Test
    public void testJob() {
		RedisLock lock = new RedisLock(redisTemplate, "TRADE_LOCK_100001");
		try {
			if (lock.acquire()) {
				//TODO 在这里实现你自己的业务逻辑
				System.out.println("to do ...");
				
			} else {
				throw new RuntimeException("can't get lock!");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("can't get lock!");
		} finally {
			lock.release();
		}
    }
    
    @Test
    public void testConcurrency() throws InterruptedException {
        final int count = 10;
        
        ConcurrentLocker[] lockers = new ConcurrentLocker[]{
                new ConcurrentLocker(count), 
                new ConcurrentLocker(count), 
                new ConcurrentLocker(count), 
                new ConcurrentLocker(count), 
                new ConcurrentLocker(count)};

        for (ConcurrentLocker locker : lockers) {
            locker.start();
        }

        for (ConcurrentLocker locker : lockers) {
            locker.join();
        }

        for (ConcurrentLocker locker : lockers) {
            assertEquals(count, locker.count());
        }
    }

    private class ConcurrentLocker extends Thread {

        private final int times;
        private int counter;
        
        public ConcurrentLocker(int times) {
            this.times = times;
            this.counter = 0;
        }
        
        public void run() {
            try {
                for (int i = 0; i < times; i++) {
                    RedisLock lock = new RedisLock(redisTemplate, "testlock");
//                    RedisLock lock = new RedisLock(redisTemplate, "testlock",5000,10000);
                    try {
                        if (lock.acquire()) {
                            counter++;
                            Thread.sleep(250);
                            lock.release();
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            } finally {
            }
        }
        
        public int count() {
            return counter;
        }
    };

    
}

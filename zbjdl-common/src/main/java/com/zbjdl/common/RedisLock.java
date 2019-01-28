package com.zbjdl.common;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisLock {
	
	private static final String LOCK_PREFIX = "";
	
	@Autowired
	private RedisTemplate<String, Long> redisTemplate;
	
	/**
	 * 获取对资源的锁
	 * @param resourceId
	 * @return
	 */
	public boolean lockResource(String resourceId) {
		Long value = redisTemplate.opsForValue().increment(LOCK_PREFIX + resourceId, 1l);
		if(value == 1l){
			redisTemplate.expire(LOCK_PREFIX + resourceId, 3, TimeUnit.SECONDS);
			return true;
		}else if(value > 1){
			for(int i=0; i<3;i++){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					return false;
				}
				if(1l == redisTemplate.opsForValue().increment(LOCK_PREFIX + resourceId, 1l)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 释放资源的锁
	 * @param resourceId
	 */
	public void releaseResource(String resourceId) {
		redisTemplate.delete(LOCK_PREFIX + resourceId);
	}
}

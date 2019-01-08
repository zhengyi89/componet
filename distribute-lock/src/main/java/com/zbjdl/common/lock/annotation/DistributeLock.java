package com.zbjdl.common.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeLock {
	/**
	 * 分布锁key
	 * 
	 * @return
	 */
	String key();

	/**
	 * 锁自动过期时间 ,单位为毫秒
	 * 
	 * @return
	 */
	int expiryTimeMillis();

	/**
	 * 获取锁最长等待时间,单位为毫秒
	 * 
	 * @return
	 */
	int acquireTimeoutMillis();

}

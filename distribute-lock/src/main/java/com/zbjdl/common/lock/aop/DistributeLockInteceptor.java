package com.zbjdl.common.lock.aop;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.zbjdl.common.lock.RedisLock;
import com.zbjdl.common.lock.annotation.DistributeLock;

/**
 * 分布式锁拦截器
 * 
 */
public class DistributeLockInteceptor implements MethodInterceptor {
	ScriptEngineManager manager = new ScriptEngineManager();
	private static final Log LOG = LogFactory.getLog(DistributeLockInteceptor.class);

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		DistributeLock distributeLock = AnnotationUtils.findAnnotation(invocation.getMethod(), DistributeLock.class);
		if (distributeLock != null) {
			String selDefKey = getSelDefKey(distributeLock.key(), invocation.getArguments());
			RedisLock lock = new RedisLock(stringRedisTemplate,selDefKey,distributeLock.acquireTimeoutMillis(), distributeLock.expiryTimeMillis());
			try {
				if (lock.acquire()) {
					return invocation.proceed();
				} else {
					throw new RuntimeException("wait for lock timeout!");
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("can't get lock!");
			} finally {
				lock.release();
			}

		} else {
			return invocation.proceed();
		}
	}

	/**
	 * 获取自定义Key
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	private String getSelDefKey(String key, Object[] args) {
		ScriptEngine engine = manager.getEngineByName("js");
		engine.put("args", args);
		try {
			return (String) engine.eval(key);
		} catch (ScriptException e) {
			LOG.debug("fail to eval express", e);
			throw new RuntimeException("fail to eval express", e);
		}
	}

}

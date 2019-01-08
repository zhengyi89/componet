package com.zbjdl.common.lock;

/**
 * 锁任务
 */
public interface LockJob {
	/**
	 * 处理任务
	 * 
	 * @param args
	 */
	Object doJob(Object... args);
	

}

/** 
 * Copyright: Copyright (c)2015
 * Company: 云宝金服
 */
package com.zbjdl.common.quata;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**    
 * @author：feng    
 * @since：2012-9-26 上午11:12:33 
 * @version:   
 */
public class QuotaBean {
	private Semaphore semaphore;
	private int permits;
	
	public QuotaBean(int _permits){
		this.semaphore = new Semaphore(_permits, true);
		this.permits = _permits;
	}
	
	public Semaphore getSemaphore() {
		return semaphore;
	}

	public int getPermits() {
		return permits;
	}
	public void setPermits(int permits) {
		this.permits = permits;
	}
	
	public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException{
		return semaphore.tryAcquire(timeout, unit);
	}
	
	public void release(){
		semaphore.release();
	}
}

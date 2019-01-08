package com.zbjdl.common.respository;

/**
 * 乐观锁异常
 */
public class OptimisticLockingException extends RuntimeException{

	private static final long serialVersionUID = 3821087967248373161L;

	public OptimisticLockingException(String message) {
		super(message);
	}

}

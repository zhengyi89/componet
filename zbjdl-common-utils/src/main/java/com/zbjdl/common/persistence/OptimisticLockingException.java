package com.zbjdl.common.persistence;

/**
 * 乐观锁异常
 * @since 2011-3-21
 * @version 1.0
 */
public class OptimisticLockingException extends RuntimeException{

	private static final long serialVersionUID = 3821087967248373161L;

	public OptimisticLockingException(String message) {
		super(message);
	}

}

package com.zbjdl.common.persistence;

/**
 * 版本不兼容异常
 * @since 2012-12-29
 * @version 1.0
 */
public class VersionUncompatableException extends RuntimeException{

	private static final long serialVersionUID = 3821087967248373161L;

	public VersionUncompatableException(String message) {
		super(message);
	}
	
	public VersionUncompatableException(String message, Throwable throwable) {
		super(message, throwable);
	}
}

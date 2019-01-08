/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview;

import com.zbjdl.common.exception.BaseException;

/**
 * 
 * @since：2012-5-19 下午10:25:17
 * @version:
 */
public class QueryUIException extends BaseException {
	public QueryUIException() {
		super();
	}

	public QueryUIException(String msg) {
		super(msg);
	}

	public QueryUIException(String msg, Throwable e) {
		super(msg, e);
	}
}

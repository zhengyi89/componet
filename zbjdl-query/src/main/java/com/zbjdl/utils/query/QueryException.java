
package com.zbjdl.utils.query;

public class QueryException extends RuntimeException{
	public QueryException(String msg){
		super(msg);
	}
	
	public QueryException(String msg, Exception cause){
		super(msg, cause);
	}
}

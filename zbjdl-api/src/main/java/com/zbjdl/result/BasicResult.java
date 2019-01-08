package com.zbjdl.result;

/**
 * 无返回值结果
 * 
 *
 */
public class BasicResult extends Result{

	private static final long serialVersionUID = 1L;
	
	public BasicResult(){
		this.setCode(Result.SUCCESS);
	}

	@Override
	public boolean isError() {
		return !Result.SUCCESS.equals(this.getCode());
	}
}

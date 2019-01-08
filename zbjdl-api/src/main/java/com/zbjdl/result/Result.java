package com.zbjdl.result;

import java.io.Serializable;

public abstract class Result implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *成功
	 */
	public static final String SUCCESS = "000000";
	
	/**
	 * 异常
	 */
	public static final String EXCEPTION = "999999";
	
	/**
	 * @return 返回码
	 */
	private String code;
	
	/**
	 * @return 返回码描述
	 */
	private String desc;
	
	public String getCode() {
		return code;
	}
	public Result setCode(String code) {
		this.code = code;
		return this;
	}
	public String getDesc() {
		if(desc == null)
			return "";
		
		return desc;
	}
	public Result setDesc(String desc) {
		this.desc = desc;
		return this;
	}
	
	public abstract boolean isError();
}

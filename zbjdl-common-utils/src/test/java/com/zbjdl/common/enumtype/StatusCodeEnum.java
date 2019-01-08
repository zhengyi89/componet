package com.zbjdl.common.enumtype;

import com.zbjdl.common.enumtype.CodeBehavior;

public enum StatusCodeEnum implements CodeBehavior {
	NORMAL("normal","正常"), WARNING("warning","警告"), ERROR("error","错误");
	//描述
	private final String description;
	//代码
	private final String code;
	private StatusCodeEnum(String code,String description) {
		this.code = code;
		this.description = description;
		
	}
	@Override
	public String getDescription() {
		return this.description;
	}
	@Override
	public String getCode() {
		return this.code;
	}
}

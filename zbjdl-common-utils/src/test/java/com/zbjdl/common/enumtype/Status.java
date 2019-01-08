package com.zbjdl.common.enumtype;

import com.zbjdl.common.enumtype.EnumBehavior;

public enum Status implements EnumBehavior {
	NORMAL("正常"), WARNING("警告"), ERROR("错误");
	//描述
	private final String description;
	private Status(String description) {
		this.description = description;
	}
	@Override
	public String getDescription() {
		return this.description;
	}

}

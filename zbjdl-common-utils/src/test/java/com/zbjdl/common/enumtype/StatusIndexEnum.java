package com.zbjdl.common.enumtype;

import com.zbjdl.common.enumtype.IndexBehavior;

public enum StatusIndexEnum implements IndexBehavior {

	NORMAL(1, "正常"), WARNING(2, "警告"), ERROR(3, "错误");
	// 描述
	private final String description;
	// 索引
	private final Integer index;

	private StatusIndexEnum(Integer index, String description) {
		this.index = index;
		this.description = description;

	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Integer getIndex() {
		return this.index;
	}

    public static  String name(int index) {  
        for (StatusIndexEnum status : StatusIndexEnum.values()) {  
            if (status.getIndex() == index) {  
                return status.name();  
            }  
        }  
        return null;  
    }
}

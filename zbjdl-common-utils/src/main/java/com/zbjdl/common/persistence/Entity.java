package com.zbjdl.common.persistence;

import java.io.Serializable;

/**
 * <p>Title: 实体接口</p>
 * <p>Description: 所有数据库实体实现该接口，泛型中转入实体ID类型</p>
 * <p>Copyright: 2011</p>
 * 
 * @author feng
 * @version 1.0,2011-1-13
 */
public interface Entity<IDClass extends java.io.Serializable> extends Serializable {

	public IDClass getId();
	
	public void setId(IDClass id);
}

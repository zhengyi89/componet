package com.zbjdl.common.respository.entity;

import java.util.Date;

/**
 * Title: 持久化领域模型基类<br/>
 * Description: 描述<br/>
 * Copyright: Copyright (c)2015<br/>
 * Company: 云宝金服<br/><br/>
 *
 */
public abstract class PersistenceEntity extends IdEntity {

	/**
	 * 创建时间
	 */
	private Date createdDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}

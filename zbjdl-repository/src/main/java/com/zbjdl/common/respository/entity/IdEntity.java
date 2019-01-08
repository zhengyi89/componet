/**
 * Copyright: Copyright (c)2015
 * Company: 云宝金服
 */
package com.zbjdl.common.respository.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Title: 领域模型基类<br/>
 * Description: 描述<br/>
 * Copyright: Copyright (c)2015<br/>
 * Company: 云宝金服<br/>
 */
public abstract class IdEntity implements Serializable {

	protected static final long serialVersionUID = -7277949963127751206L;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}

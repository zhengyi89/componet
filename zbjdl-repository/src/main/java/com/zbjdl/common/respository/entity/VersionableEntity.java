/**
 * Copyright: Copyright (c)2015
 * Company: 云宝金服
 */
package com.zbjdl.common.respository.entity;

import java.util.Date;

/**
 * Title: 可版本化的领域模型基类<br/>
 * Description: 描述<br/>
 * Copyright: Copyright (c)2015<br/>
 * Company: 云宝金服<br/><br/>
 *
 */
public abstract class VersionableEntity extends PersistenceEntity {

	private Long version = 0L;

	/**
	 * 最后修改时间时间
	 */
	private Date lastModifiedDate;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}

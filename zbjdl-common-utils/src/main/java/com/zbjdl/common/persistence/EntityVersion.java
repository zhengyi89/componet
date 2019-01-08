package com.zbjdl.common.persistence;

/**
 * <p>Title: 实体版本接口</p>
 * <p>Description: 所有要实现乐观锁的数据库实体实现该接口，泛型中转入实体ID类型</p>
 * <p>Copyright: 2011</p>
 * 
 * @author feng
 * @version 1.0,2011-1-13
 */
public interface EntityVersion<IDClass extends java.io.Serializable> extends Entity<IDClass> {

	public void setVersion(Long version);
	
	public Long getVersion();
}

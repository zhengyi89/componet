package com.zbjdl.database;

import org.mybatis.spring.SqlSessionTemplate;


/**
 * 数据库操作基类，使用时需要注入SqlSessionFactory
 * 
 *
 */
public class BaseSlaveDao {
	private SqlSessionTemplate sqlSessionTemplate;

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
}
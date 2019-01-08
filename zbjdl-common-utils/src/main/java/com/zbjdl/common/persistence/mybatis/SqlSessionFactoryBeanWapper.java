package com.zbjdl.common.persistence.mybatis;

import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * 继承SqlSessionFactoryBean用于在初始化完SqlSessionFactory后<br/>
 * 调用Configuration.buildAllStatements()来构建所有Statement<br/>
 * 以解决高并发时Statement重复构建的问题
 *
 * @author: ning
 * @version: 1.0
 * @since: 2011-8-22
 */
@Deprecated
public class SqlSessionFactoryBeanWapper extends SqlSessionFactoryBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		// 兼容 mybatis 3.0.4 & 3.2.8
//        getObject().getConfiguration().buildAllStatements();
		getObject().getConfiguration().getMappedStatementNames();
	}
}

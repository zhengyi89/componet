package com.zbjdl.utils.query.jdbc;

import javax.sql.DataSource;

/**
 * JdbcQueryer工厂
 */
public class JdbcQueryerFactory {
	
	public static IJdbcQueryer createJdbcQueryer(DataSource dataSource){
		return new JdbcQueryer(dataSource);
	}
}

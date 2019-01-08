package com.zbjdl.utils.query.jdbc;

import java.util.List;
import java.util.Map;



/**
 * Jdbc查询器
 * 
 */
public interface IJdbcQueryer {

	/**
	 * 分页结果中带总数
	 * @param dataSource
	 * @param startIndex
	 * @param maxSize
	 * @param sql
	 * @param sqlParams
	 * @return
	 */
	public JdbcQueryResult query(Integer currentPage, Integer startIndex,
			Integer maxSize, String sql, List sqlParams);

	/**
	 * 分页结果中不带总数
	 * @param dataSource
	 * @param startIndex
	 * @param maxSize
	 * @param sql
	 * @param sqlParams
	 * @param counter
	 * @return
	 */
	public List queryList(Integer currentPage, Integer startIndex,
			Integer maxSize, String sql, List sqlParams);
	
	/**
	 * 非分页查询
	 * @param dataSource
	 * @param sql
	 * @param sqlParams
	 * @return
	 */
	public List query(String sql, List sqlParams);
	
	/**
	 * 非分页，查询结果为唯一
	 * @param dataSource
	 * @param sql
	 * @param sqlParams
	 * @param checkUnique
	 * @return
	 */
	public Map queryUniqueRow(String sql, List sqlParams, boolean checkUnique);
}

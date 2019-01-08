
package com.zbjdl.utils.query;


public class Query {
	/**
	 * 查询SQL表达式
	 */
	private String sql;
	/**
	 * 查询数据源
	 */
	private String dataSource;
	/**
	 * 统计SQL表达式
	 */
	private String sumSelect;
	/**
	 * 查询条件为空时是否执行查询SQL
	 */
	private boolean queryWithoutParam = true;
	/**
	 * 根据ognl表达式计算结果判断是否执行SQL
	 */
	private String queryableExp;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getSumSelect() {
		return sumSelect;
	}
	public void setSumSelect(String sumSelect) {
		this.sumSelect = sumSelect;
	}
	public boolean isQueryWithoutParam() {
		return queryWithoutParam;
	}
	public void setQueryWithoutParam(boolean queryWithoutParam) {
		this.queryWithoutParam = queryWithoutParam;
	}
	public String getQueryableExp() {
		return queryableExp;
	}
	public void setQueryableExp(String queryableExp) {
		this.queryableExp = queryableExp;
	}
}

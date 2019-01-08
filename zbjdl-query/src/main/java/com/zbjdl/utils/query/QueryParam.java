
package com.zbjdl.utils.query;

import java.util.Map;


public class QueryParam {
	
	/**
	 * 查询的起始行
	 */
	private Integer startIndex;

	/**
	 * 查询返回的最大记录数
	 */
	private Integer maxSize;

	/**
	 * 查询参数
	 */
	private Map<String, Object> params;

	/**
	 * 排序字段
	 */
	private String orderByColumn;

	/**
	 * 是否升序
	 */
	private Boolean isAsc;

	/**
	 * 是否执行统计SQL
	 */
	private Boolean doSum;
	
	
	
	/**
	 * 当前页
	 */
	private Integer currentPage;
	
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	public Integer getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}
	public Map getParams() {
		return params;
	}
	public void setParams(Map params) {
		this.params = params;
	}
	public String getOrderByColumn() {
		return orderByColumn;
	}
	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}
	public Boolean getIsAsc() {
		return isAsc;
	}
	public void setIsAsc(Boolean isAsc) {
		this.isAsc = isAsc;
	}
	public Boolean getDoSum() {
		return doSum;
	}
	public void setDoSum(Boolean doSum) {
		this.doSum = doSum;
	}
}

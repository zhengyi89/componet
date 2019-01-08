
package com.zbjdl.utils.query;

import java.util.Collection;
import java.util.Map;



public class QueryResult {
	private Collection<? extends Map<String, Object>> data;
	
	private Long totalCount;
	
	private Integer startIndex;
	
	private Integer maxFetchSize;
	
	private String orderStr;
	
	private Boolean isAsc;
	
	private String queryKey;
	
	private Map sumData;
	

	public QueryResult(Collection a_data, Long a_totalCount){
		this.data = a_data;
		this.totalCount = a_totalCount;
	}
	
	/**    
	 * data    
	 *    
	 * @return  the data   
	 */
	
	public Collection<? extends Map<String, Object>> getData() {
		return data;
	}
	

	/**    
	 * 描述： 在这里描述属性的含义
	 * @param data the data to set    
	 */
	public void setData(Collection<? extends Map<String, Object>> data) {
		this.data = data;
	}

	/**    
	 * totalCount    
	 *    
	 * @return  the totalCount   
	 */
	
	public Long getTotalCount() {
		return totalCount;
	}

	/**    
	 * 描述： 总记录数
	 * @param totalCount the totalCount to set    
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**    
	 * orderStr    
	 *    
	 * @return  the orderStr   
	 */
	
	public String getOrderStr() {
		return orderStr;
	}

	/**    
	 * 描述： 在这里描述属性的含义
	 * @param orderStr the orderStr to set    
	 */
	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	/**    
	 * isAsc    
	 *    
	 * @return  the isAsc   
	 */
	
	public Boolean getIsAsc() {
		return isAsc;
	}

	/**    
	 * 描述： 在这里描述属性的含义
	 * @param isAsc the isAsc to set    
	 */
	public void setIsAsc(Boolean isAsc) {
		this.isAsc = isAsc;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	/**
	 * 查询的起始行数
	 * @param startIndex
	 */
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getMaxFetchSize() {
		return maxFetchSize;
	}

	/**
	 * 查询的最大获取行数
	 * @param maxFetchSize
	 */
	public void setMaxFetchSize(Integer maxFetchSize) {
		this.maxFetchSize = maxFetchSize;
	}

	public String getQueryKey() {
		return queryKey;
	}

	/**
	 * 查询key
	 * @param queryKey
	 */
	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public Map getSumData() {
		return sumData;
	}

	public void setSumData(Map sumData) {
		this.sumData = sumData;
	}
}

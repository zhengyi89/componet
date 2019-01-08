
package com.zbjdl.utils.query.jdbc;

import java.util.List;


public class JdbcQueryResult {
	private List data;
	private long totalCount;

	public JdbcQueryResult(List a_data){
		this.data = a_data;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	

	public List getData() {
		return data;
	}

	public long getTotalCount() {
		return totalCount;
	}
}

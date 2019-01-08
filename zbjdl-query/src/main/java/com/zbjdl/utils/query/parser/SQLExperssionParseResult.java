
package com.zbjdl.utils.query.parser;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;


public class SQLExperssionParseResult {

	private String sql;
	private List sqlParams;
	private boolean hasConditions;
	
	public SQLExperssionParseResult(String sql, boolean hasConditions){
		this.sql = sql;
		this.hasConditions = hasConditions;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setHasConditions(boolean hasConditions) {
		this.hasConditions = hasConditions;
	}

	public void setSqlParams(List sqlParams) {
		this.sqlParams = sqlParams;
	}

	public String getSql() {
		return sql;
	}

	public List getSqlParams() {
		return sqlParams;
	}

	public boolean hasConditions() {
		return hasConditions;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}


package com.zbjdl.utils.query.parser;
import java.util.List;

public class QueryRequest {
	private List sqlParams;
	private SimpleSQLStatement sqlStatement;
	private boolean hasCondition;

	@Deprecated
	public QueryRequest(SimpleSQLStatement statement, List params){
		sqlStatement = statement;
		sqlParams = params;
	}

	public QueryRequest(SimpleSQLStatement statement, List params, boolean hasCondition){
		this.sqlStatement = statement;
		this.sqlParams = params;
		this.hasCondition = hasCondition;
	}

	public List getSqlParams() {
		return sqlParams;
	}

	public SimpleSQLStatement getSqlStatement() {
		return sqlStatement;
	}

	public boolean hasCondition() {
		return hasCondition;
	}

	public void setHasCondition(boolean hasCondition) {
		this.hasCondition = hasCondition;
	}

}

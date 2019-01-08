
package com.zbjdl.utils.query.parser;

import com.zbjdl.common.utils.CheckUtils;

/** 
 * SQL语句的解析结果   
 */
public class SimpleSQLStatement {
	private String select;
	private String from;
	private String where;
	private String groupBy;
	private String orderBy;
	private String withUr;
	
	public SimpleSQLStatement(String a_select, String a_from, String a_where, String a_groupBy, String a_orderBy, String a_withUr){
		select = a_select;
		from = a_from;
		where = a_where;
		groupBy = a_groupBy;
		orderBy = a_orderBy;
		withUr = a_withUr;
	}
	
	public String getSQL(){
		return (trimStr(select) + trimStr(from) + trimStr(where) + trimStr(groupBy) + trimStr(orderBy) + trimStr(withUr)).trim();
	}

	public SimpleSQLStatement replaceOrderBy(String newOrderBy){
		return new SimpleSQLStatement(select, from, where, groupBy, newOrderBy, withUr);
	}
	
	public SimpleSQLStatement replaceSelect(String newSelect){
		return new SimpleSQLStatement(newSelect, from, where, groupBy, orderBy, withUr);
	}
	
	public SimpleSQLStatement retriveSumSql(String newSelect){
		return new SimpleSQLStatement(newSelect, from, where, null, null, withUr);
	}
	
	private String trimStr(String str){
		if(!CheckUtils.isEmpty(str)){
			return " "+str.trim();
		}
		return "";
	}

	public String getSelect() {
		return select;
	}
	
	public String getFrom() {
		return from;
	}

	public String getWhere() {
		return where;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public String getWithUr() {
		return withUr;
	}
}

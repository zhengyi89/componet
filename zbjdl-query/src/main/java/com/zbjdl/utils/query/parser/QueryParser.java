
package com.zbjdl.utils.query.parser;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QueryParser {

	private final static Logger logger = LoggerFactory.getLogger(QueryParser.class);

	/**
	 * 解析SQL表达式
	 * @param sqlExpression
	 * @param context
	 * @return
	 */
	public static QueryRequest parse(String sqlExpression, Map context){
		//step1:把SQL表达式转换成标准SQL，并获得sqlParam
		SQLExperssionParseResult sqlResult = SQLExperssionParser.parseSQLExpression(sqlExpression, context, true);
		
		//step2:把标准SQL解析成SimpleSQLStatement对象(已缓存)
		SimpleSQLStatement statement = SimpleSQLParser.parse(sqlResult.getSql());
		
		//step3:组装返回结果
		return new QueryRequest(statement, sqlResult.getSqlParams(), sqlResult.hasConditions());
	}
	
	/**
	 * 只解析select部分的SQL表达式
	 * @param selectExpression
	 * @param context
	 * @return
	 */
	public static String parseSelectOnly(String selectExpression, Map context){
		return SQLExperssionParser.parseSQLExpression(selectExpression, context, false).getSql();
	}
}

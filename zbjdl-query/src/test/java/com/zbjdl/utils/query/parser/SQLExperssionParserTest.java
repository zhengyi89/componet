
package com.zbjdl.utils.query.parser;

import com.zbjdl.utils.query.QueryException;
import com.zbjdl.utils.query.parser.SQLExperssionParseResult;
import com.zbjdl.utils.query.parser.SQLExperssionParser;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class SQLExperssionParserTest {

	@Test
	public void testParseSQLExpression1() throws Exception {
		String query = "select id,TRXID,REALAMOUNT,SETTLEFLAG,BATCHID,HISTORYTYPE,TRXAMOUNT,DESCRIPTION,ACCOUNT_ID,CREATEDATE from accounthistory"
				+"/~startDate: and createDate >={startDate,sqldate}~/"
				+"/~ endDate : and createDate < '[endDate,sqldate]'~/"
				+"/~id: and id={id,long}~/";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startDate", "2010-09-01");
		context.put("endDate", "2010-10-01");
		context.put("endDate1", "abc");
		context.put("endDate2", "xxxxxxx");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
		System.out.println(result.getSql());
		System.out.println(result.getSqlParams());
	}

	@Test
	public void testParseSQLExpression2() throws Exception {
		String sql = "select * from table where "+
				"/~startDate: price between {startDate}~/\n" +
				"/~endDate: and {endDate}~/\r";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startDate", "2010-09-01");
		context.put("endDate", "2010-10-01");
		context.put("endDate1", "abc");
		context.put("endDate2", "xxxxxxx");
		SQLExperssionParseResult result2 = SQLExperssionParser.parseSQLExpression(sql, context, true);
		System.out.println(result2.getSql());
		System.out.println(result2.getSqlParams());
	}

	@Test(expected = QueryException.class)
	public void testParseSQLExpression4() throws Exception {
		String query = "select id,TRXID,REALAMOUNT,SETTLEFLAG,BATCHID,HISTORYTYPE,TRXAMOUNT,DESCRIPTION,ACCOUNT_ID,CREATEDATE from accounthistory"
				+"/~startDate: and createDate >={startDate,sqldate}~/"
				+"/~ endDate : and createDate < '[endDate]'~/"
				+"/~id: and id={id,long}~/";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startDate", "2010-09-01");
		context.put("endDate", "2010-09-30 bitandnot(0,1)");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
	}

	@Test(expected = QueryException.class)
	public void testParseSQLExpression6() throws Exception {
		String query = "select id,TRXID,REALAMOUNT,SETTLEFLAG,BATCHID,HISTORYTYPE,TRXAMOUNT,DESCRIPTION,ACCOUNT_ID,CREATEDATE from accounthistory"
				+"/~startDate: and createDate >={startDate,sqldate}~/"
				+"/~ endDate : and createDate < '[endDate]'~/"
				+"/~id: and id={id,long}~/";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startDate", "2010-09-01");
		context.put("endDate", "2010-09-30 select * into");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
	}

	@Test(expected = QueryException.class)
	public void testParseSQLExpression7() throws Exception {
		String query = "select id,TRXID,REALAMOUNT,SETTLEFLAG,BATCHID,HISTORYTYPE,TRXAMOUNT,DESCRIPTION,ACCOUNT_ID,CREATEDATE from accounthistory"
				+"/~startDate: and createDate >={startDate,sqldate}~/"
				+"/~ endDate : and createDate < '[endDate]'~/"
				+"/~id: and id={id,long}~/";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startDate", "2010-09-01");
		context.put("endDate", "2010-09-30 update table1 set a=1");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
	}

	@Test(expected = QueryException.class)
	public void testParseSQLExpression8() throws Exception {
		String query = "select id,TRXID,REALAMOUNT,SETTLEFLAG,BATCHID,HISTORYTYPE,TRXAMOUNT,DESCRIPTION,ACCOUNT_ID,CREATEDATE from accounthistory"
				+"/~startDate: and createDate >={startDate,sqldate}~/"
				+"/~ endDate : and createDate < '[endDate]'~/"
				+"/~id: and id={id,long}~/";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startDate", "2010-09-01");
		context.put("endDate", "2010-09-30 delete  from table1");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
	}

	@Test(expected = QueryException.class)
	public void testParseSQLExpression9() throws Exception {
		String query = "select el.*,ds.ds_name from TBL_DB_LOG el" +
				" inner join TBL_DB_DATASOURCE ds on el.log_ds_id=ds.ds_id" +
				" where 1=1" +
				" /~logLevel: and el.log_level = {logLevel,String}~/" +
				" /~logDsId: and el.log_ds_id = {logDsId,long}~/" +
				" /~schema: and el.log_schema = {schema,String}~/" +
				" /~sqlStatus: and el.log_sql_status = {sqlStatus,String}~/" +
				" /~logExecutor: and el.log_executor like '%'||{logExecutor}||'%'~/" +
				" /~startTime: and el.log_start_datetime >= '[startTime,mintimestamp]'~/" +
				" /~endTime: and el.log_start_datetime <= '[endTime,maxtimestamp]'~/" +
				" /~startScore: and el.log_score >= '[startScore,long]'~/" +
				" /~endScore: and el.log_score <= '[endScore,long]'~/" +
				" /~queryStr: and el.log_query <= '[queryStr]'~/" +
				" order by el.log_id desc" +
				" with ur";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startTime", "2010-09-01");
		context.put("queryStr", ";select user from sysibm.sysdummy1");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
	}

	@Test(expected = QueryException.class)
	public void testParseSQLExpression10() throws Exception {
		String query = "select el.*,ds.ds_name from TBL_DB_LOG el" +
				" inner join TBL_DB_DATASOURCE ds on el.log_ds_id=ds.ds_id" +
				" where 1=1" +
				" /~logLevel: and el.log_level = {logLevel,String}~/" +
				" /~logDsId: and el.log_ds_id = {logDsId,long}~/" +
				" /~schema: and el.log_schema = {schema,String}~/" +
				" /~sqlStatus: and el.log_sql_status = {sqlStatus,String}~/" +
				" /~logExecutor: and el.log_executor like '%'||{logExecutor}||'%'~/" +
				" /~startTime: and el.log_start_datetime >= '[startTime,mintimestamp]'~/" +
				" /~endTime: and el.log_start_datetime <= '[endTime,maxtimestamp]'~/" +
				" /~startScore: and el.log_score >= '[startScore,long]'~/" +
				" /~endScore: and el.log_score <= '[endScore,long]'~/" +
				" /~queryStr: and el.log_query <= '[queryStr,string]'~/" +
				" order by el.log_id desc" +
				" with ur";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startTime", "2010-09-01");
		context.put("queryStr", ";select user from sysibm.sysdummy1");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
	}

	@Test
	public void testParseSQLExpression11() throws Exception {
		String query = "select el.*,ds.ds_name from TBL_DB_LOG el" +
				" inner join TBL_DB_DATASOURCE ds on el.log_ds_id=ds.ds_id" +
				" where 1=1" +
				" /~logLevel: and el.log_level = {logLevel,String}~/" +
				" /~logDsId: and el.log_ds_id = {logDsId,long}~/" +
				" /~schema: and el.log_schema = {schema,String}~/" +
				" /~sqlStatus: and el.log_sql_status = {sqlStatus,String}~/" +
				" /~logExecutor: and el.log_executor like '%'||{logExecutor}||'%'~/" +
				" /~startTime: and el.log_start_datetime >= '[startTime,mintimestamp]'~/" +
				" /~endTime: and el.log_start_datetime <= '[endTime,maxtimestamp]'~/" +
				" /~startScore: and el.log_score >= '[startScore,long]'~/" +
				" /~endScore: and el.log_score <= '[endScore,long]'~/" +
				" /~queryStr: and el.log_query <= '[queryStr,,disabled]'~/" +
				" order by el.log_id desc" +
				" with ur";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startTime", "2010-09-01");
		context.put("queryStr", ";select user from sysibm.sysdummy1");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
	}

	@Test
	public void testParseSQLExpression12() throws Exception {
		String query = "select el.*,ds.ds_name from TBL_DB_LOG el" +
				" inner join TBL_DB_DATASOURCE ds on el.log_ds_id=ds.ds_id" +
				" where 1=1" +
				" /~logLevel: and el.log_level = {logLevel,String}~/" +
				" /~logDsId: and el.log_ds_id = {logDsId,long}~/" +
				" /~schema: and el.log_schema = {schema,String}~/" +
				" /~sqlStatus: and el.log_sql_status = {sqlStatus,String}~/" +
				" /~logExecutor: and el.log_executor like '%'||{logExecutor}||'%'~/" +
				" /~startTime: and el.log_start_datetime >= '[startTime,mintimestamp]'~/" +
				" /~endTime: and el.log_start_datetime <= '[endTime,maxtimestamp]'~/" +
				" /~startScore: and el.log_score >= '[startScore,long]'~/" +
				" /~endScore: and el.log_score <= '[endScore,long]'~/" +
				" /~queryStr: and el.log_query <= '[queryStr,,disabled]'~/" +
				" order by el.log_id desc" +
				" with ur";
		Map<String, String> context = new HashMap<String, String>();
		context.put("id", "1");
		context.put("startTime", "2010-09-01");
		context.put("queryStr", "select  into");
		SQLExperssionParseResult result = SQLExperssionParser.parseSQLExpression(query, context, true);
	}

}


package com.zbjdl.utils.query.parser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zbjdl.common.utils.CheckUtils;



/**
 * 简单的SQL解析工具，从标准SQL语句中识别出select,where,group by,order by,with ur这几个部分
 * 
 * 对于类似
 * 	 select name , 'select a from b where 1=1' from user where id=1 
 * 这样的SQL语句不能正常识别
 * 
 * 对于子查询语句，例如 
 * 	select name from user where id in (select user_id from customer ) 
 * 可以正常识别
 * 
 */
public class SimpleSQLParser {

	private final static Pattern FROM_EXP = Pattern.compile("[\\s,\\)]FROM[\\s,\\(]");
	private final static Pattern WHERE_EXP = Pattern.compile("[\\s,\\)]WHERE[\\s,\\(]");
	private final static Pattern ORDERBY_EXP = Pattern.compile("[\\s,\\)]ORDER\\s+BY[\\s,\\(]");
	private final static Pattern GROUPBY_EXP = Pattern.compile("[\\s,\\)]GROUP\\s+BY[\\s,\\(]");
	private final static Pattern WITHUR_EXP = Pattern.compile("[\\s,\\)]WITH\\s+UR[\\s]?");

	/**
	 * 缓存 SQL 解析结果
	 */
	private final static ConcurrentMap<String, SimpleSQLStatement> parseResultMap = new ConcurrentHashMap<String, SimpleSQLStatement>();

	public static SimpleSQLStatement parse(String sql){
		// 查询缓存
		SimpleSQLStatement statement = parseResultMap.get("sql");
		if (null == statement) {
			CheckUtils.notEmpty(sql, "sql");
			String select = null;
			String from  = null;
			String where = null;
			String orderBy = null;
			String groupBy = null;
			String withUr = null;

			//转换成大写来匹配
			String upperCaseSql = sql.toUpperCase();
			//从后向前截取

			//截取withur部分
			int withUrIndex = findWithUr(upperCaseSql);
			if(withUrIndex>0){
				withUr = sql.substring(withUrIndex).trim();
				sql = sql.substring(0, withUrIndex);
				upperCaseSql = upperCaseSql.substring(0, withUrIndex);
			}

			//截取orderby部分
			int orderByIndex = findOrderBy(upperCaseSql);
			if(orderByIndex>0){
				orderBy = sql.substring(orderByIndex).trim();
				sql = sql.substring(0, orderByIndex);
				upperCaseSql = upperCaseSql.substring(0, orderByIndex);
			}

			//截取groupby部分
			//2017-03-28 修改获取 groupby部分，要截取最后一个groupby ，否则如果中间有 子查询包含groupby 查询就出错了。而且 如果最后没有groupby 仅在子查询中有group by 也会出现问题
			int groupByIndex = findGroupBy(upperCaseSql);
			if(groupByIndex>0){
				groupBy = sql.substring(groupByIndex).trim();//如果group by sql 存在关联 where 等，则会有问题。
				String tempUpperGroupSql = groupBy.toUpperCase();
				int whereIndex = findWhere(tempUpperGroupSql);
				if(whereIndex>0){
					groupBy = ""; 
				}else{
					sql = sql.substring(0, groupByIndex);
					upperCaseSql = upperCaseSql.substring(0, groupByIndex);
				}
			}

			//截取where部分
			int whereIndex = findWhere(upperCaseSql);
			if(whereIndex>0){
				where = sql.substring(whereIndex).trim();
				sql = sql.substring(0, whereIndex);
				upperCaseSql = upperCaseSql.substring(0, whereIndex);
			}

			// 截取from 部分
			int fromIndex = findFrom(upperCaseSql);
			if(fromIndex>0){
				from = sql.substring(fromIndex).trim();
				sql = sql.substring(0, fromIndex);
				upperCaseSql = upperCaseSql.substring(0, fromIndex);
			}

			//剩下select部分
			select = sql.trim();
			statement = new SimpleSQLStatement(select, from, where, groupBy, orderBy, withUr);

			// 更新缓存
			parseResultMap.put(sql, statement);
		}
		return statement;
	}
	
	private static int findWhere(String sql){
		Matcher m = WHERE_EXP.matcher(sql);
		if(m.find()){
			String tmp = m.group();
			int index = m.start();
			int tmp_index = tmp.indexOf("W");
			return index+tmp_index;
		}else{
			return -1;
		}
	}

	private static int findFrom(String sql){
		Matcher m = FROM_EXP.matcher(sql);
		if(m.find()){
			String tmp = m.group();
			int index = m.start();
			int tmp_index = tmp.indexOf("F");
			return index+tmp_index;
		}else{
			return -1;
		}
	}

	private static int findOrderBy(String sql){
		return findLast(sql, ORDERBY_EXP, "O");
	}

	private static int findGroupBy(String sql){
		return findLast(sql, GROUPBY_EXP, "G");
	}

	private static int findWithUr(String sql){
		return findLast(sql, WITHUR_EXP, "W");
	}

	private static int findLast(String sql, Pattern pattern, String flag){
		Matcher m = pattern.matcher(sql);
		String tmp = null;
		int index = -1;
		int tmp_index = 1;
		while(m.find()){
			tmp = m.group();
			index = m.start();
			tmp_index = tmp.indexOf(flag);
		}
		return index+tmp_index;
	}

	public static String parseOrderBy(String columnStr, String ascStr){
		if(CheckUtils.isEmpty(columnStr)){
			return null;
		}
		String orderBy = "order by ";
		String defaultAsc = "asc";
		String[] columns = columnStr.split(",");
		String[] ascs = null;
		if(!CheckUtils.isEmpty(ascStr)){
			ascs = ascStr.split(",");
		}
		for(int i=0; i<columns.length; i++){
			String column = columns[i];
			String asc = null;
			if(ascs!=null && ascs.length>=i){
				try{
					asc = Boolean.valueOf(ascs[i])?"asc":"desc";
					defaultAsc = asc;
				}catch(Exception e){
					asc = defaultAsc;
				}
			}else{
				asc = defaultAsc;
			}
			orderBy += column +" "+ asc + ",";
		}
		return orderBy.substring(0, orderBy.length()-1);
	}

}

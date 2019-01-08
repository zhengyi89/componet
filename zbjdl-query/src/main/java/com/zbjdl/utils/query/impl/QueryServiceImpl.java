
package com.zbjdl.utils.query.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.utils.query.Query;
import com.zbjdl.utils.query.QueryException;
import com.zbjdl.utils.query.QueryParam;
import com.zbjdl.utils.query.QueryResult;
import com.zbjdl.utils.query.QueryService;
import com.zbjdl.utils.query.jdbc.IJdbcQueryer;
import com.zbjdl.utils.query.jdbc.JdbcQueryResult;
import com.zbjdl.utils.query.jdbc.JdbcQueryerFactory;
import com.zbjdl.utils.query.parser.QueryParser;
import com.zbjdl.utils.query.parser.QueryRequest;
import com.zbjdl.utils.query.parser.SimpleSQLParser;
import com.zbjdl.utils.query.utils.OgnlUtils;
import com.zbjdl.utils.query.utils.QueryLogUtils;


public class QueryServiceImpl implements QueryService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String,Query> querys;
	private Map<String,DataSource> dataSourceMap;
	private DataSource dataSource;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.utils.query.QueryService#query(java.lang.Integer,
	 * java.lang.Integer, java.lang.String, java.util.Map, java.lang.String,
	 * java.lang.Boolean)
	 */
	@Override
	public List query(Integer startIndex, Integer maxResult, String queryKey,
			Map<String, Object> param, String orderStr, Boolean isAsc) {
		QueryParam queryParam = new QueryParam();
		queryParam.setStartIndex(startIndex);
		queryParam.setMaxSize(maxResult);
		queryParam.setParams(param);
		queryParam.setOrderByColumn(orderStr);
		queryParam.setIsAsc(isAsc);
		return queryList(queryKey, queryParam);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.utils.query.QueryService#query(java.lang.Integer,
	 * java.lang.Integer, java.lang.String, java.util.Map, java.lang.Boolean,
	 * java.lang.String, java.lang.Boolean)
	 */
	@Override
	public QueryResult query(Integer startIndex, Integer maxResult,
			String queryKey, Map<String, Object> param,
			String orderStr, Boolean isAsc,  Boolean doSum) {
		QueryParam queryParam = new QueryParam();
		queryParam.setStartIndex(startIndex);
		queryParam.setMaxSize(maxResult);
		queryParam.setParams(param);
		queryParam.setOrderByColumn(orderStr);
		queryParam.setIsAsc(isAsc);
		queryParam.setDoSum(doSum);
		return query(queryKey, queryParam);
	}

	public Map querySum(String queryKey, Map<String, Object> param) {
		Query queryConfig = findQuery(queryKey);
		DataSource ds = findDataSource(queryConfig.getDataSource());
		QueryRequest queryRequest = QueryParser.parse(queryConfig.getSql(), param);
		return doSum(queryKey, queryConfig, queryRequest, ds, param);
	}

	private Map doSum(String queryKey, Query queryConfig, QueryRequest queryRequest, DataSource ds, Map param){
		if(CheckUtils.isEmpty(queryConfig.getSumSelect())){
			throw new QueryException("no sum sql found ! queryKey["+queryKey+"]");
		}
		String sumSelect = QueryParser.parseSelectOnly(queryConfig.getSumSelect(), param);
		IJdbcQueryer queryer = JdbcQueryerFactory.createJdbcQueryer(ds);
		Map sumData = queryer.queryUniqueRow(queryRequest.getSqlStatement().retriveSumSql(sumSelect).getSQL(), queryRequest.getSqlParams(), true);
		return sumData;
	}

	@Override
	public List query(String queryKey, Map<String, Object> param) {
		return query(queryKey, param, false);
	}
	
	public List query(String queryKey, Map<String, Object> param,
			boolean isForDal) {
		return query(queryKey, param, null, false, isForDal);
	}
	@Override
	public List query(String queryKey, Map<String, Object> param,String sortColumn,boolean isAsc){
		return query(queryKey,param,sortColumn,isAsc,false);
	}

	
	public List query(String queryKey, Map<String, Object> param, String sortColumn,boolean isAsc,
			boolean isForDal) {
		long startTime = System.currentTimeMillis();

		Query queryConfig = findQuery(queryKey);
		DataSource ds = findDataSource(queryConfig.getDataSource());
		QueryRequest queryRequest = createQueryRequest(queryKey,queryConfig, param);
		if(queryRequest==null){
			return null;
		}
		// 增加排序
		String sql = null;
		if (sortColumn != null) {
			String orderBy = SimpleSQLParser.parseOrderBy(sortColumn, isAsc
					+ "");
			if (!CheckUtils.isEmpty(orderBy)) {
				sql = queryRequest.getSqlStatement()
						.replaceOrderBy(orderBy).getSQL();
			}else{
				sql = queryRequest.getSqlStatement().getSQL();
			}
		}else{
			sql = queryRequest.getSqlStatement().getSQL();
		}
		IJdbcQueryer queryer = JdbcQueryerFactory.createJdbcQueryer(ds);
		
		List res = queryer.query(sql, queryRequest.getSqlParams());
		
		//add 2014-12-26 增加对SQL查询时间的监控 begin
		try {
			QueryLogUtils.wasteTimeLog(queryKey, queryConfig.getSql(), sql, param, queryRequest.getSqlParams(), startTime);
		} catch (Throwable e) {
			logger.error("查询组件监控时间报错", e);
		}
		//add 2014-12-26 增加对SQL查询时间的监控 end
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.utils.query.QueryService#queryUnique(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public Map queryUnique(String queryKey, Map<String, Object> param, boolean checkUnique) {
		return queryUnique(queryKey, param, checkUnique, false);
	}

	public Map queryUnique(String queryKey, Map<String, Object> param, boolean checkUnique, boolean isForDal){
		Query queryConfig = findQuery(queryKey);
		if(queryConfig==null){
			throw new QueryException("no query defined with key : "+queryKey);
		}
		DataSource ds = findDataSource(queryConfig.getDataSource());
		QueryRequest queryRequest = QueryParser.parse(queryConfig.getSql(), param);
		IJdbcQueryer queryer = JdbcQueryerFactory.createJdbcQueryer(ds);
		return queryer.queryUniqueRow(queryRequest.getSqlStatement().getSQL(), queryRequest.getSqlParams(), checkUnique);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.utils.query.QueryService#query(java.lang.String,
	 * com.zbjdl.utils.query.QueryParam)
	 */
	@Override
	public QueryResult query(String queryKey, QueryParam queryParam) {
		long startTime = System.currentTimeMillis();

		Query queryConfig = findQuery(queryKey);
		if(queryConfig==null){
			throw new QueryException("no query defined with key : "+queryKey);
		}
		DataSource ds = findDataSource(queryConfig.getDataSource());
		QueryRequest queryRequest = createQueryRequest(queryKey,queryConfig, queryParam.getParams());
		if(queryRequest==null){
			return null;
		}

		String orderBy = SimpleSQLParser.parseOrderBy(queryParam.getOrderByColumn(), queryParam.getIsAsc()!=null?queryParam.getIsAsc().toString():null);
		String sql = null;
		if(!CheckUtils.isEmpty(orderBy)){
			sql = queryRequest.getSqlStatement().replaceOrderBy(orderBy).getSQL();
		}else{
			sql = queryRequest.getSqlStatement().getSQL();
		}

		IJdbcQueryer queryer = JdbcQueryerFactory.createJdbcQueryer(ds);
		JdbcQueryResult jdbcResult = queryer.query(queryParam.getCurrentPage(), queryParam.getStartIndex(), queryParam.getMaxSize(), sql, queryRequest.getSqlParams());
		QueryResult result = new QueryResult(jdbcResult.getData(), jdbcResult.getTotalCount());
		result.setQueryKey(queryKey);
		result.setStartIndex(queryParam.getStartIndex());
		result.setMaxFetchSize(queryParam.getMaxSize());
		result.setOrderStr(queryParam.getOrderByColumn());
		result.setIsAsc(queryParam.getIsAsc());
		if(queryParam.getDoSum()!=null && queryParam.getDoSum()){
			result.setSumData(doSum(queryKey, queryConfig, queryRequest, ds, queryParam.getParams()));
		}
		
		//add 2014-12-26 增加对SQL查询时间的监控 begin
		try {
			QueryLogUtils.wasteTimeLog(queryKey, queryConfig.getSql(), sql, queryParam.getParams(), queryRequest.getSqlParams(), startTime);
		} catch (Throwable e) {
			logger.error("查询组件监控时间报错", e);
		}
		//add 2014-12-26 增加对SQL查询时间的监控 end
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.utils.query.QueryService#queryList(java.lang.String,
	 * com.zbjdl.utils.query.QueryParam)
	 */
	@Override
	public List queryList(String queryKey, QueryParam queryParam) {
		long startTime = System.currentTimeMillis();
		
		Query queryConfig = findQuery(queryKey);
		DataSource ds = findDataSource(queryConfig.getDataSource());
		QueryRequest queryRequest = createQueryRequest(queryKey,queryConfig, queryParam.getParams());
		if(queryRequest==null){
			return null;
		}
		String orderBy = SimpleSQLParser.parseOrderBy(queryParam.getOrderByColumn(), queryParam.getIsAsc()!=null?queryParam.getIsAsc().toString():null);
		String sql = null;
		if(!CheckUtils.isEmpty(orderBy)){
			sql = queryRequest.getSqlStatement().replaceOrderBy(orderBy).getSQL();
		}else{
			sql = queryRequest.getSqlStatement().getSQL();
		}
		IJdbcQueryer queryer = JdbcQueryerFactory.createJdbcQueryer( ds);
		
		
		List res = queryer.queryList(queryParam.getCurrentPage(), queryParam.getStartIndex(), queryParam.getMaxSize(), sql, queryRequest.getSqlParams());
		
		//add 2014-12-26 增加对SQL查询时间的监控 begin
		try {
			QueryLogUtils.wasteTimeLog(queryKey, queryConfig.getSql(), sql, queryParam.getParams(), queryRequest.getSqlParams(), startTime);
		} catch (Throwable e) {
			logger.error("查询组件监控时间报错", e);
		}
		//add 2014-12-26 增加对SQL查询时间的监控 end
		return res;
	}

	private QueryRequest createQueryRequest(String queryKey, Query queryConfig, Map<String, Object> params){
		if("false".equals(params.get("_queryable"))){
			return null;
		}
		QueryRequest queryRequest = QueryParser.parse(queryConfig.getSql(), params);
		
		if(!queryConfig.isQueryWithoutParam() && !queryRequest.hasCondition()){
			return null;
		}
		if(!CheckUtils.isEmpty(queryConfig.getQueryableExp())){
			// 优化性能 1.5:1
			Map<String, Object> _params = new HashMap<String, Object>();
			for(Map.Entry<String, Object> item : params.entrySet()) {
				Object value = item.getValue();
				if(!CheckUtils.isEmpty(value)){
					_params.put(item.getKey(), value);
				}
			}
			Object queryable = OgnlUtils.executeExpression(queryConfig.getQueryableExp(), _params);
			if(!Boolean.TRUE.equals(queryable)){
				return null;
			}
		}
		return queryRequest;
	}

	public Query findQuery(String key){
		if(querys==null){
			throw new QueryException("querys is empty !");
		}

		Query query = querys.get(key);
		if(query==null){
			throw new QueryException("no query found : "+key);
		}
		return query;
	}

	private DataSource findDataSource(String dsName){
		if(dsName == null){
			if(dataSource==null){
				throw new QueryException("no default dataSource found !");
			}
			return dataSource;
		}
		if(dataSourceMap==null || !dataSourceMap.containsKey(dsName)){
			throw new QueryException("no dataSource found : "+dsName);
		}
		return dataSourceMap.get(dsName);
	}

	public void setQuerys(Map<String, Query> querys) {
		this.querys = querys;
	}

	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
}

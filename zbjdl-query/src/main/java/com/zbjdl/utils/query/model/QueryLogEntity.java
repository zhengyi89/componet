package com.zbjdl.utils.query.model;

import java.io.Serializable;
import java.util.Date;

import com.zbjdl.common.annotation.LogColumn;
import com.zbjdl.common.annotation.LogTable;

/**
 * <p>Description: 记录查询组件中 最消耗时间的查询</p>     
 */
@LogTable(tableName = "TBL_QUERY_LOG")
public class QueryLogEntity implements Serializable {
	
	private static final long serialVersionUID = 4535814696082116376L;

	/**
	 * ID
	 */
	@LogColumn(name = "ID")
	private Long id;
	
	/**
	 * 应用名
	 */
	@LogColumn(name = "APPLICATION")
	private String application;

	/**
	 * GUID
	 */
	@LogColumn(name = "GUID")
	private String guid;
	
	/**
	 * 日志类型
	 */
	@LogColumn(name = "LOG_TYPE")
	private LogTypeEnum logType;
	
	/**
	 * 查询queryKey
	 */
	@LogColumn(name = "QUERY_KEY")
	private String queryKey;
	
	/**
	 * 查询模板
	 */
	@LogColumn(name = "QUERY_TEMPLETE")
	private String queryTemplete;
	
	/**
	 * 最终执行SQL
	 */
	@LogColumn(name = "QUERY_SQL")
	private String querySql;
	
	/**
	 * 请求参数
	 */
	@LogColumn(name = "REQUEST_PARAM")
	private String requestParam;
	
	/**
	 * SQL查询参数
	 */
	@LogColumn(name = "SQL_PARAM")
	private String sqlParam;
	
	/**
	 * 查询执行时间
	 */
	@LogColumn(name = "QUERY_TIME")
	private Long queryTime;
	
	/**
	 * 创建时间
	 */
	@LogColumn(name = "CREATE_TIME")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public String getQueryTemplete() {
		return queryTemplete;
	}

	public void setQueryTemplete(String queryTemplete) {
		this.queryTemplete = queryTemplete;
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public Long getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Long queryTime) {
		this.queryTime = queryTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public LogTypeEnum getLogType() {
		return logType;
	}

	public void setLogType(LogTypeEnum logType) {
		this.logType = logType;
	}
	
	
	

}

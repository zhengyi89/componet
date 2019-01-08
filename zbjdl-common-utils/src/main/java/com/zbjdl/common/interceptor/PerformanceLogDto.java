package com.zbjdl.common.interceptor;

import java.io.Serializable;
import java.util.Date;

import com.zbjdl.common.annotation.LogColumn;
import com.zbjdl.common.annotation.LogTable;
@LogTable(tableName = "performance_log")
public class PerformanceLogDto implements Serializable {
	private static final long serialVersionUID = -413698099765231201L;

	/**
	 * 主键
	 */
	@LogColumn(name = "ID")
	private Long id;



	/**
	 * 创建时间
	 */
	@LogColumn(name = "CREATE_TIME")
	private Date createTime;
	
	

	/**
	 * 类名
	 */
	@LogColumn(name = "SERVICE_NAME")
	private String serviceName;
	/**
	 * 方法名
	 */
	@LogColumn(name = "METHOD_NAME")
	private String methodName;
	
	/**
	 * 开始时间
	 */
	@LogColumn(name = "BEGIN_TIME")
	private Date beginTime;
	/**
	 * 结束时间
	 */
	@LogColumn(name = "END_TIME")
	private Date endTime;

	/**
	 * 耗时时间(毫秒)
	 */
	@LogColumn(name = "ELAPSED_TIME")
	private Long elapsedTime;		

	@LogColumn(name = "IP")
	private String ip;

	@LogColumn(name = "PARAM_CONTENT")
	private String paramContent;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getParamContent() {
		return paramContent;
	}

	public void setParamContent(String paramContent) {
		this.paramContent = paramContent;
	}	
}

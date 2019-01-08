package com.zbjdl.common.interceptor;

import java.io.Serializable;
import java.util.Date;

import com.zbjdl.common.annotation.LogColumn;
import com.zbjdl.common.annotation.LogTable;

@LogTable(tableName = "message_log")
public class MessageLogDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7165575187670371272L;

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
	
	@LogColumn(name = "REMOTE_IP")
	private String remoteIp;
	
	@LogColumn(name = "URL")
	private String url;

	@LogColumn(name = "REQ_CONTENT")
	private String requestContent;
	
	/**
	 * 响应报文
	 */
	@LogColumn(name = "RESP_CONTENT")
	private String responseContent;
	
	/**
	 * 返回码
	 */
	@LogColumn(name = "RSP_CODE")
	private String rspCode;
	
	/**
	 * 返回码说明
	 */
	@LogColumn(name = "RSP_MSG")
	private String rspMsg;
	
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

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}	
}
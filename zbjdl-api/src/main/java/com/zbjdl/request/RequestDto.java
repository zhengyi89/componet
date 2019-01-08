package com.zbjdl.request;

import java.io.Serializable;

/**
 * 支付请求参数基类
 * 
 *
 */
public abstract class RequestDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 请求流水号
	 */
	private String requestNo;
	private String token;
	private RequestTypeEnum requestType;
	private String sysId;
	private String busiTypeId;
	private String gateRouteId;
	private String pmCode;

	/**
	 * 业务大类编码
	 */
	private String bizType;
	
	/**
	 * 支付通道ID
	 */
	private String payChannelId;
	
	public String getRequestNo() {
		return requestNo;
	}
	/**
	 * 请求流水号
	 * 如不设定，系统默认自动生成32位随机数
	 */
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	/**
	 * 请求业务类型
	 */
	public RequestTypeEnum getRequestType() {
		return requestType;
	}
	/**
	 * @required
	 * 请求业务类型
	 */
	public void setRequestType(RequestTypeEnum requestType) {
		this.requestType = requestType;
	}
	/**
	 * 请求令牌
	 */
	public String getToken() {
		return token;
	}
	/**
	 * 请求令牌
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * 发起请求系统id
	 */
	public String getSysId() {
		return sysId;
	}
	/**
	 * @required
	 * 发起请求系统id
	 * 由多渠道接入系统分配
	 */
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	/**
	 * 支付请求业务编码
	 */
	public String getBusiTypeId() {
		return busiTypeId;
	}
	/**
	 * @required
	 * 支付请求业务编码
	 * 由多渠道接入前置分配
	 */
	public void setBusiTypeId(String busiTypeId) {
		this.busiTypeId = busiTypeId;
	}
	/**
	 * 多渠道系统标识
	 * @return gateRouteId
	 */
	public String getGateRouteId() {
		return gateRouteId;
	}
	/**
	 * 多渠道系统标识
	 * @param gateRouteId
	 */
	public void setGateRouteId(String gateRouteId) {
		this.gateRouteId = gateRouteId;
	}
	/**
	 * 支付方式
	 * @return pmCode
	 */
	public String getPmCode() {
		return pmCode;
	}
	/**
	 * 支付方式
	 * @param pmCode
	 */
	public void setPmCode(String pmCode) {
		this.pmCode = pmCode;
	}

	/**
	 * 得到业务大类编码
	 * @return
	 */
	public String getBizType() {
		return bizType;
	}

	/**
	 * 设置业务大类编码
	 * @param bizType
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	/**
	 * 获取支付通道ID<p>
	 * @return  payChannelId  支付通道ID<br>
	 */
	public String getPayChannelId() {
		return payChannelId;
	}
	/**
	 * 设置支付通道ID<p>
	 * @param  payChannelId  支付通道ID<br>
	 */
	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}
	@Override
	public String toString() {
		return "RequestDto [payChannelId="+payChannelId+"requestNo=" + requestNo + ", token=" + token + ", requestType=" + requestType + ", sysId=" + sysId + ", busiTypeId=" + busiTypeId + ", gateRouteId=" + gateRouteId + ",pmCode=" + pmCode + "]";
	}
	
}

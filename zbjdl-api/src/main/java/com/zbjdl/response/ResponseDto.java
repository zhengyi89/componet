package com.zbjdl.response;

import java.io.Serializable;

/**
 * 支付请求响应基类
 * 
 *
 */
public abstract class ResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String returnCode;
	private String returnMsg;
	private String requestNo;
	private String sysId;
	private String busiTypeId;
	private String pmCode;
	/**
	 * 业务大类编码
	 */
	private String bizType;
	
	/**
	 * 支付通道ID
	 */
	private String payChannelId;
	
	/**
	 * 请求流水号
	 */
	public String getRequestNo() {
		return requestNo;
	}
	/**
	 * 请求流水号
	 */
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	/**
	 * 发起请求系统id
	 */
	public String getSysId() {
		return sysId;
	}
	/**
	 * 发起请求系统id
	 */
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	/**
	 * 请求业务类型
	 */
	public String getBusiTypeId() {
		return busiTypeId;
	}
	/**
	 * 请求业务类型
	 */
	public void setBusiTypeId(String busiTypeId) {
		this.busiTypeId = busiTypeId;
	}
	/**
	 * 返回码
	 */
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * 返回码
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * 返回码描述
	 */
	public String getReturnMsg() {
		return returnMsg;
	}
	/**
	 * 返回码描述
	 */
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getPmCode() {
		return pmCode;
	}
	public void setPmCode(String pmCode) {
		this.pmCode = pmCode;
	}
	
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getPayChannelId() {
		return payChannelId;
	}
	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}
	@Override
	public String toString() {
		return "ResponseDto [returnCode=" + returnCode + ", returnMsg=" + returnMsg + ", requestNo=" + requestNo + ", sysId=" + sysId + ", busiTypeId=" + busiTypeId + ", pmCode=" + pmCode + "]";
	}
	
}

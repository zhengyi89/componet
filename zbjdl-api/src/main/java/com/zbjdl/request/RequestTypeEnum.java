package com.zbjdl.request;


/**
 * 请求类型枚举类
 * 
 *
 */
public enum RequestTypeEnum {
	/**
	 * 查询
	 */
	QUERY("QUERY"),
	
	/**
	 * 收款
	 */
	RECEIPT("RECEIPT"),
	/**
	 * 付款
	 */
	PAY("PAY"),
	
	/**
	 * 账户验证
	 */
	ACCOUNT_VERIFY("ACCOUNT_VERIFY"),
	
	/**
	 * 协议上送
	 */
	CONTRACT_UPLOAD("CONTRACT_UPLOAD"),
	
	/**
	 * 快捷支付
	 */
	FAST_PAY("FAST_PAY"),
	
	/**
	 * 校验短信验证码
	 */
	CHECK_SMS_CODE("CHECK_SMS_CODE"),
	
	/**
	 * 重新发送短信验证码
	 */
	RESEND_SMS_CODE("RESEND_SMS_CODE"),
	
	/**
	 * B2C支付
	 */
	B2C_PAY("B2C_PAY"),
	
	/**
	 * 订单查询
	 */
	ORDER_QUERY("ORDER_QUERY"),
	
	/**
	 * 订单取消
	 */
	ORDER_CANCEL("ORDER_CANCEL"),
	
	/**
	 * 订单退款
	 */
	ORDER_REFUND("REFUND"),
	
	/**
	 * B2B支付
	 */
	B2B_PAY("B2B_PAY"),
	
	/**
	 * 基金申购
	 */
	FUND_BUY("FUND_BUY"),
	
	/**
	 * 基金赎回
	 */
	FUND_REDEEM("FUND_REDEEM"),
	
	/**
	 * 鉴权
	 */
	AC("AC"),
	
	/**
	 * 签约
	 */
	SIGN("SIGN"),
	
	/**
	 * 签约
	 */
	SURRENDER("SURRENDER"),
	/**
	 * 提现
	 */
	WITHDRAW("WITHDRAW"),
	/**
	 * 代扣
	 */
	WITHHOLD("WITHHOLD"),
	/**
	 * 退款
	 */
	REFUND("REFUND");
	
	
	private RequestTypeEnum(String requestTypeCode){
		this.requestTypeCode = requestTypeCode;
	}
	
	private String requestTypeCode;

	public String getRequestTypeCode() {
		return requestTypeCode;
	}
	
	public static RequestTypeEnum toEnum(String requestTypeCode) {
		for(RequestTypeEnum item : RequestTypeEnum.values()) {
			if(item.getRequestTypeCode().equalsIgnoreCase(requestTypeCode)) {
				return item;
			}
		}
		return null;
	}
}

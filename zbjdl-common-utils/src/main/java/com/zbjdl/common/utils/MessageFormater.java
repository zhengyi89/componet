package com.zbjdl.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 格式转换辅助类，主要在页面以及报表工具中调用，例如:
 * VelocityEngine.put("formater", new MessageFormater());
 * @since：2011-5-31 下午03:35:08 
 * @version:   
 */
public class MessageFormater {
	private static Logger logger = LoggerFactory.getLogger(MessageFormater.class);
	public static String DEFAULT_PATTERNMODE_DATE = "datetime";
	public static String DEFAULT_PATTERNMODE_NUMBER = "amount";
	private Map<String,String> patterns;
	private Map<String,Integer> multipliers;
	private RoundingMode roundingMode;
	
	public MessageFormater(){
		this("properties/messageFormater.properties");
	}

	public MessageFormater(String properties){
		patterns = CommonUtils.loadProps(properties);
		if(patterns==null){
			throw new RuntimeException("init messageFormater fail! no patterns.properties found!");
		}
		try{
			roundingMode = RoundingMode.valueOf(patterns.get("roundingMode"));
		}catch(Exception e){
		}
		if(roundingMode==null){
			throw new RuntimeException("init messageFormater fail! no roundingMode specified!");
		}
		multipliers = new HashMap<String,Integer>();
		for(String key : patterns.keySet()){
			if(key.endsWith(".multiplier")){
				try{
					Integer m = Integer.parseInt(patterns.get(key));
					multipliers.put(key.substring(0, key.length()-11), m);
				}catch(Exception e){
					throw new RuntimeException("init messageFormater fail! parse multiplier fail : "+key+"="+patterns.get(key)); 
				}
			}
		}
	}
	
	public String formatDate(Object date){
			return formatDate(date, DEFAULT_PATTERNMODE_DATE, null);
	}
	
	public String formatDate(Object date, String _patternMode){
		String pattern = patterns.get(_patternMode);
		CheckUtils.notEmpty(pattern, "pattern");
		return formatDateWithPattern(date, pattern, null);
	}
	
	public String formatDate(Object date, String _patternMode, String timeZone){
		String pattern = patterns.get(_patternMode);
		CheckUtils.notEmpty(pattern, "pattern");
		return formatDateWithPattern(date, pattern, timeZone);
	}
	
	public String formatDateWithPattern(Object date, String pattern, String timeZone){
		TimeZone _timeZone = null;
		if(!CheckUtils.isEmpty(timeZone)){
			_timeZone = TimeZone.getTimeZone(timeZone);
		}
		try{
			return FormatUtils.formatDate(date, pattern, _timeZone);
		}catch(Exception e){
			logger.warn("formatDate error : "+e.getMessage());
			return null;
		}
	}
	
	public String formatNumber(Object num){
		return formatNumber(num, DEFAULT_PATTERNMODE_NUMBER);
	}
	
	public String formatNumber(Object num, String _patternMode){
		String pattern = patterns.get(_patternMode);
		Integer muliter = multipliers.get(_patternMode);
		if(muliter==null){
			muliter = 1;
		}
		return formatNumber(num, pattern, roundingMode, muliter);
	}
	
	public String formatNumber(Object num, String _patternMode, String _roundingMode){
		String pattern = patterns.get(_patternMode);
		Integer muliter = multipliers.get(_patternMode);
		if(muliter==null){
			muliter = 1;
		}
		return formatNumber(num, pattern, RoundingMode.valueOf(_roundingMode), muliter);
	}
	
	public String formatNumber(Object num, String pattern, RoundingMode roundingMode, int multiplier){
		try{
			return FormatUtils.formatNumber(num, pattern, roundingMode, multiplier);
		}catch(Exception e){
			logger.warn("formatNumber error : "+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 格式化成金额输出
	 * @param num
	 * @return
	 */
	public String formateMoney(Object num){
		return formatNumber(num, "money");
	}
	
	/**
	 * 格式化成金额输出,输入货币符号(￥)
	 * @param num
	 * @return
	 */
	public String formateAmount(Object num){
		if(num == null)
			num = BigDecimal.ZERO;
		return formatNumber(num, "signAmount");
	}
	
	public String escapeHtml(String str){
		return StringEscapeUtils.escapeHtml(str);
	}
	
	public String escapeJavaScript(String str){
		return StringEscapeUtils.escapeJavaScript(str);
	}
	
	public String escapeXml(String str){
		return StringEscapeUtils.escapeXml(str);
	}
	
	/**
	 * 格式化手机号以便显示
	 * @param cellphoneNo
	 * @return
	 */
	@Deprecated
	public String formatCellphone(String cellphoneNo){
		return FormatUtils.formatCellphone(cellphoneNo);
	}
	
	/**
	 * 隐藏邮箱地址便于显示
	 * @param email
	 * @return
	 */
	@Deprecated
	public String formatEmail(String email){
		return FormatUtils.formatEmail(email);
	}
	
	/**
	 * 隐藏邮箱地址便于显示
	 * @param email
	 * @return
	 */
	public String maskEmail(String email){
		return FormatUtils.formatEmail(email);
	}
	
	/**
	 * 隐藏手机号以便显示
	 * @param cellphoneNo
	 * @return
	 */
	public String maskCellphone(String cellphoneNo){
		return FormatUtils.formatCellphone(cellphoneNo);
	}
	
	/**
	 * 隐藏身份证号码
	 * @param cardNo
	 * @return
	 */
	public String maskIDCardNo(String idCardNo){
		if(CheckUtils.isEmpty(idCardNo)){
			return idCardNo;
		}
		return MaskUtils.maskIDCardNo(idCardNo);
	}
	
	/**
	 * 隐藏银行卡号码
	 * @param cardNo
	 * @return
	 */
	public String maskBankCardNo(String bankCardNo){
		if(CheckUtils.isEmpty(bankCardNo)){
			return bankCardNo;
		}
		return MaskUtils.maskBankCardNo(bankCardNo);
	}
	
	/**
	 * 隐藏登录名
	 * @param loginName
	 * @return
	 */
	public String maskLoginName(String loginName){
		if(CheckUtils.isEmpty(loginName)){
			return loginName;
		}
		if(ValidateUtils.isEmail(loginName)){
			return MaskUtils.maskEmail(loginName);
		}
		if(ValidateUtils.isMobile(loginName)){
			return MaskUtils.maskCellphone(loginName);
		}
		return loginName;
	}
	

}

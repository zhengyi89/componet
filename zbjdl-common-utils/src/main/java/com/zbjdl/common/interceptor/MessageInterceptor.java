package com.zbjdl.common.interceptor;

import java.lang.reflect.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zbjdl.common.log.SoaLogUtils;
import com.zbjdl.common.utils.DateUtils;
import com.zbjdl.common.utils.IpUtils;
import com.zbjdl.common.utils.StringUtils;

/**
 * @author xwy
 */
public class MessageInterceptor implements MethodInterceptor {
	
	private final static Logger logger = LoggerFactory.getLogger(MessageInterceptor.class);
	private final ScheduledExecutorService logSendTimer = Executors
			.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	private List<String> includeMethods;

	private List<String> excludeMethods;

	@Override
	public Object invoke(MethodInvocation method) throws Throwable {
		final Date beginTime = new Date();
		final long start = beginTime.getTime();
		Object respObj = new Object();
		try {
			Object returnObj = method.proceed();
			respObj = returnObj;
			return returnObj;
		} finally {
			try {
				final Date endTime = new Date();
				final String methodName = method.getMethod().getName();
				final String serviceName = method.getMethod().getDeclaringClass().getName();
				final Object[] args = method.getArguments();

				String remoteIp = StringUtils.EMPTY;
				String reqUrl = StringUtils.EMPTY;
				JSONArray paramArr = new JSONArray();
				int arrInd = 0;
				for (int i = 0; i < args.length; i++) {
					Object fieldValue = args[i];
					if (fieldValue instanceof HttpServletRequest) {
						HttpServletRequest request = (HttpServletRequest)fieldValue;
						remoteIp = IpUtils.getIpAddr(request);
						reqUrl = request.getRequestURL().toString();
					}else{
						try{
							paramArr.add(arrInd, JSON.toJSONString(fieldValue));
						}catch(Exception e){
							logger.info("to jsonstring exception,记录了原始入参信息，监控时会解析不出来此类数据");
							paramArr.add(arrInd, fieldValue);
						}
						arrInd++;
					}
				}
				final String paramString = paramArr.toString();
				
				String respStr = JSON.toJSONString(respObj);
				String rspCode = StringUtils.EMPTY;
				String rspMsg = StringUtils.EMPTY;
				try{
					JSONObject respJsonObj = JSON.parseObject(respStr);
					JSONObject bodyObj = null;
					if(respJsonObj.get("body") instanceof JSONObject){
						bodyObj = (JSONObject) respJsonObj.get("body");
					}else{
						String bodyStr =   (String) respJsonObj.get("body");
						if(StringUtils.isNotEmpty(bodyStr)){
							bodyObj = JSON.parseObject(bodyStr);
						}
					}
					if(null != bodyObj){
						respStr = bodyObj.toJSONString();
						rspCode = (String) bodyObj.get("rspCode");
						rspMsg = (String) bodyObj.get("rspMsg");
						if(StringUtils.isEmpty(rspCode)){
							rspCode = (String) bodyObj.get("returnCode");
						}
						if(StringUtils.isEmpty(rspMsg)){
							rspMsg = (String) bodyObj.get("returnMsg");
						}
					}else{
						if(null != respJsonObj.get("rspCode")){
							rspCode = respJsonObj.get("rspCode").toString();
						}
						if(null != respJsonObj.get("rspMsg")){
							rspMsg = respJsonObj.get("rspMsg").toString();
						}
						if(StringUtils.isEmpty(rspCode) && null != respJsonObj.get("returnCode")){
							rspCode = respJsonObj.get("returnCode").toString();
						}
						if(StringUtils.isEmpty(rspMsg) && null != respJsonObj.get("returnMsg")){
							rspMsg = respJsonObj.get("returnMsg").toString();
						}
					}
				}catch(Exception e){
					logger.info("parse json exception,返回码没有解析出来，记录了空值");
				}
				
				final String rspCodeF = rspCode;
				final String rspMsgF = rspMsg;
				final String remoteIpF = remoteIp;
				final String reqUrlF = reqUrl;
				final String respStrF = respStr;
				
				logSendTimer.schedule(new Runnable() {
					public void run() {
						long end = endTime.getTime();
						long elapsed = end - start;
						if (matched(methodName)) {
							MessageLogDto logDto = new MessageLogDto();
							Set<String> ipSet = IpUtils.resolveLocalIps();
							logDto.setIp((ipSet.size() > 0 ? ipSet.iterator().next() : null));
							logDto.setServiceName(serviceName);
							logDto.setMethodName(methodName);
							logDto.setElapsedTime(elapsed);
							logDto.setBeginTime(beginTime);
							logDto.setEndTime(endTime);
							logDto.setRequestContent(paramString);
							logDto.setResponseContent(respStrF);
							logDto.setRspCode(rspCodeF);
							logDto.setRspMsg(rspMsgF);
							logDto.setRemoteIp(remoteIpF);
							logDto.setUrl(reqUrlF);
							
							SoaLogUtils.save(this.getClass().getName(), logDto);
						}
					}
				}, 1, TimeUnit.SECONDS);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public void setIncludeMethods(List<String> methodNames) {
		if (methodNames != null && !methodNames.isEmpty()) {
			includeMethods = new ArrayList<String>();
			// 将通配符表达式换成正则表达式
			// 比如：send* --> send.*
			for (String pattern : methodNames) {
				if (pattern != null) {
					includeMethods.add(pattern.replaceAll("\\.*\\*", ".*"));
				}
			}
		}
	}

	public void setExcludeMethods(List<String> methodNames) {
		if (methodNames != null && !methodNames.isEmpty()) {
			excludeMethods = new ArrayList<String>();
			// 将通配符表达式换成正则表达式
			// 比如：send* --> send.*
			for (String pattern : methodNames) {
				if (pattern != null) {
					excludeMethods.add(pattern.replaceAll("\\.*\\*", ".*"));
				}
			}
		}
	}

	private boolean matched(String methodName) {
		// 优先匹配include配置
		if (includeMethods != null && !includeMethods.isEmpty()) {
			for (String pattern : includeMethods) {
				if (methodName.matches(pattern)) {
					return true;
				}
			}
			return false;
		}
		// 没有配置include再匹配exclude
		if (excludeMethods != null && !excludeMethods.isEmpty()) {
			for (String pattern : excludeMethods) {
				if (methodName.matches(pattern)) {
					return false;
				}
			}
			return true;
		}
		// include&exclude都没有配置则返回true
		return true;
	}

	@SuppressWarnings("unused")
	private static Object getFieldValue(Class<?> fieldType, Object fieldValue)
			throws NumberFormatException, SQLException {
		if (fieldValue == null) {
			return null;
		}
		if (fieldType.isArray()) {
			return getArrayFieldValue(fieldValue);
		}
		return getNormalFieldValue(fieldType, fieldValue);
	}

	private static String getBlobFieldValue(Object fieldValue) throws NumberFormatException, SQLException {
		Blob blob = (Blob) fieldValue;
		byte[] datas = blob.getBytes(0L, Integer.parseInt(String.valueOf(blob.length())));
		return new String(datas);
	}

	private static String getClobFieldValue(Object fieldValue) throws NumberFormatException, SQLException {
		Clob clob = (Clob) fieldValue;
		return clob.getSubString(0L, Integer.parseInt(String.valueOf(clob.length())));
	}

	private static Object getArrayFieldValue(Object fieldValue) throws NumberFormatException, SQLException {
		String result = null;
		StringBuffer sb = new StringBuffer();
		int length = Array.getLength(fieldValue);
		for (int i = 0; i < length; i++) {
			Object value = Array.get(fieldValue, i);
			Class<?> valueType = value.getClass();
			Object fvalue = getNormalFieldValue(valueType, value);
			sb.append(fvalue);
		}
		result = sb.toString();
		return result;
	}

	private static Object getNormalFieldValue(Class<?> fieldType, Object fieldValue)
			throws NumberFormatException, SQLException {
		if (fieldValue == null || fieldValue instanceof String) {
			return fieldValue;
		}
		if (fieldValue instanceof Enum) {
			return ((Enum<?>) fieldValue).name();
		}
		if (fieldValue instanceof Blob) {
			return getBlobFieldValue(fieldValue);
		}
		if (fieldValue instanceof Clob) {
			return getClobFieldValue(fieldValue);
		}
		if (fieldValue instanceof Date) {
			return getDateFieldValue(fieldValue);
		}
		if (fieldValue instanceof Timestamp) {
			return getTimestampFieldValue(fieldValue);
		}
		if (fieldValue instanceof Boolean) {
			return getBooleanFieldValue(fieldValue);
		}
		if (fieldType.getName().startsWith("com.")) {
			return StringUtils.trim(ToStringBuilder.reflectionToString(fieldValue, ToStringStyle.SHORT_PREFIX_STYLE)
					.replace("\n", " ").replace("\t", "  "));
		}
		return fieldValue;
	}

	private static Object getDateFieldValue(Object fieldValue) {
		Date date = (Date) fieldValue;
		return DateUtils.toString(date, DateUtils.DATE_FORMAT_DATETIME);
	}

	private static Object getTimestampFieldValue(Object fieldValue) {
		Timestamp stamp = (Timestamp) fieldValue;
		return DateUtils.toSqlTimestampString(stamp, DateUtils.DATE_FORMAT_DATETIME);
	}

	private static Object getBooleanFieldValue(Object fieldValue) {
		Boolean b = (Boolean) fieldValue;
		return b ? 1 : 0;
	}
}

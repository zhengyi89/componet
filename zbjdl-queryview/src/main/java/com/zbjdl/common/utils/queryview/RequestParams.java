/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview;

import com.zbjdl.common.utils.ConvertUtils;
import com.zbjdl.common.utils.queryview.interceptor.ControllerContext;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-16 19:46
 */
public class RequestParams {

	protected static final Logger LOGGER = LoggerFactory.getLogger(RequestParams.class);

	private Map params = new HashMap();

	public static RequestParams buildRequestParams() {
		RequestParams requestParams = new RequestParams();
		if (null == ControllerContext.getContext().getRequest()) {
			LOGGER.error("Kid, your must declaration DataTablesHandlerInterceptor in your spring-mvc.xml");
		} else {
			retrieveParams(requestParams.params, ControllerContext.getContext().getRequest().getParameterMap());
		}
		retrieveParams(requestParams.params, ControllerContext.getContext().getContextMap());
		return requestParams;
	}

	private static Map retrieveParams(Map params, Map requestParams) {
		for (Object key : requestParams.keySet()) {
			if (ControllerContext.REQUEST.equals(key)
					|| ControllerContext.RESPONSE.equals(key)) {
				continue;
			}
			Object value = requestParams.get(key);
			if (value != null) {
				if (value.getClass().isArray()) {
					int length = Array.getLength(value);
					if (length == 1) {
						params.put(key, Array.get(value, 0));
					}
					if (length > 1) {
						params.put(key, value);
					}
				} else {
					params.put(key, value);
				}
			}
		}
		return params;
	}

	public Long getLongParam(String key) {
		return (Long) convertParam("long", key);
	}

	public Integer getIntegerParam(String key) {
		return (Integer) convertParam("int", key);
	}

	public Date getDateParam(String key) {
		return (Date) convertParam("date", key);
	}

	public Double getDoubleParam(String key) {
		return (Double) convertParam("double", key);
	}

	public Boolean getBooleanParam(String key) {
		return (Boolean) convertParam("boolean", key);
	}

	public Object convertParam(String toType, String key) {
		String value = getStringParam(key);
		if (value == null) {
			return null;
		}
		try {
			return ConvertUtils.convert(toType, value);
		} catch (Exception e) {
			return null;
		}
	}

	public String getStringParam(String key) {
		Object value = params.get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		} else {
			return ((String[]) value)[0];
		}
	}

	public Object getParam(String key) {
		return params.get(key);
	}

	public Map getParams() {
		return params;
	}

}

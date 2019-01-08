/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview;

import com.zbjdl.common.utils.queryview.tags.bean.PreparedParamBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since：2012-6-4 下午05:47:07
 * @version:
 */
public class QueryHelperParam {

	private String queryKey;
	private String serviceName;
	private Integer pageSize;
	private boolean doSum;
	private boolean autoTrim = true;
	private List<PreparedParamBean> preparedParams;

	public QueryHelperParam(String queryKey) {
		this.queryKey = queryKey;
	}

	public void addPreparedParam(String key, Object value) {
		addPreparedParam(key, value, true);
	}

	public void addPreparedParam(String key, Object value, boolean prefered) {
		if (preparedParams == null) {
			preparedParams = new ArrayList<PreparedParamBean>();
		}
		PreparedParamBean pb = new PreparedParamBean(key, value, prefered);
		preparedParams.add(pb);
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isDoSum() {
		return doSum;
	}

	public void setDoSum(boolean doSum) {
		this.doSum = doSum;
	}

	public boolean isAutoTrim() {
		return autoTrim;
	}

	public void setAutoTrim(boolean autoTrim) {
		this.autoTrim = autoTrim;
	}

	public List<PreparedParamBean> getPreparedParams() {
		return preparedParams;
	}
}

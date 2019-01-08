/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview;

/**
 * 
 * @since：2012-6-4 下午05:46:48
 * @version:
 */
public class QueryHelper {

	public static QueryForm executeQuery(QueryHelperParam param) {
		QueryUIConfig config = QueryUIConfig.getConfiguration();
		return QueryExecutor.executeQuery(param.getQueryKey(), config.getQueryService(param.getServiceName()),
				config.getPageSize(param.getPageSize()), param.isDoSum(),
				param.isAutoTrim(), param.getPreparedParams());
	}

}

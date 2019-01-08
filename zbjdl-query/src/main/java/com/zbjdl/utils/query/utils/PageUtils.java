package com.zbjdl.utils.query.utils;

import java.util.HashMap;
import java.util.Map;

import com.zbjdl.utils.query.QueryParam;
import com.zbjdl.utils.query.QueryResult;
/**
 * 分页工具类
 * 
 */
public class PageUtils {
	/**
	 * 每页显示数量
	 */
	protected static final String PAGE_SIZE = "20";

	/**
	 * 初始化查询组件参数，无条件查询
	 * 
	 * @param param
	 */
	public static QueryParam initQueryParam() {
		return initQueryParam(null, null);
	}

	/**
	 * 初始化查询组件参数
	 * 
	 * @param param
	 */
	public static QueryParam initQueryParam(QueryParam queryParam, Map<String, Object> paramMap) {
		if (queryParam == null) {
			queryParam = new QueryParam();
		}
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
		queryParam.setParams(paramMap);
		if (queryParam.getMaxSize() == null) {
			queryParam.setMaxSize(Integer.valueOf(PAGE_SIZE));
		}
		if (queryParam.getCurrentPage() == null) {
			queryParam.setCurrentPage(1);
		}
		if (queryParam.getStartIndex() == null) {
			queryParam.setStartIndex((queryParam.getCurrentPage() - 1) * queryParam.getMaxSize() + 1); // 开始条数
		}
		return queryParam;
	}

	/**
	 * 计算最大页数
	 * 
	 * @param queryResult
	 * @return
	 */
	public static long getTotalPage(QueryResult queryResult) {
		long maxPageNo = 1;
		if (queryResult.getTotalCount() % queryResult.getMaxFetchSize() == 0) {
			maxPageNo = queryResult.getTotalCount() / queryResult.getMaxFetchSize();
		} else {
			maxPageNo = queryResult.getTotalCount() / queryResult.getMaxFetchSize() + 1;
		}
		return maxPageNo;
	}
}

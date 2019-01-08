package com.zbjdl.common.utils.queryview;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.queryview.tags.bean.PreparedParamBean;
import com.zbjdl.utils.query.QueryParam;
import com.zbjdl.utils.query.QueryResult;
import com.zbjdl.utils.query.QueryService;
import com.zbjdl.utils.query.parser.SQLExperssionParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * <p>Title: 查询执行者</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-16 19:52
 */
public class QueryExecutor {

	protected static final Logger LOGGER = LoggerFactory.getLogger(QueryExecutor.class);

	//设定阈值，单位为ms
	private static final long THRESHOLD = 1200;

	public static QueryForm executeQuery(String queryKey, String queryService, Integer pageSize, Boolean doSum, Boolean doTrim, List<PreparedParamBean> preparedParams) {
		CheckUtils.notEmpty(queryKey, "queryKey");
		CheckUtils.notEmpty(queryService, "queryService");
		long startTime = System.currentTimeMillis();

		QueryService service = null;
		try {
			
			WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
			if(webApplicationContext != null ){
				service = (QueryService) webApplicationContext.getBean(queryService);

			}else{
				ApplicationContext applicationContext = QueryViewApplicationContextHelper.getContext();
				service = (QueryService) applicationContext.getBean(queryService);

			}
			
		} catch (Exception e) {
			throw new QueryUIException("get queryService bean fail, queryServiceName[" + queryService + "] message : " + e.getMessage(), e);
		}
		QueryForm queryForm = QueryForm.buildForm(queryKey);
		queryForm.setPageSize(pageSize);

		Map<String, Object> httpParams = queryForm.getHttpParams();
		if ("false".equals(httpParams.get("_queryable"))) {
			return queryForm;
		}

		// 检查用户请求参数是否包含可疑请求字符
		for (Map.Entry<String, Object> entry : httpParams.entrySet()) {
			// 防注入
			Object value = entry.getValue();
			if (value instanceof String) {
				Matcher m = SQLExperssionParser.sqlInjectionParrern.matcher(((String) value).toLowerCase());
				if (m.find()) {
					LOGGER.warn("注入信息：param:{}, value:{}", entry.getKey(), value);
					entry.setValue(entry.getKey() + ":@" + value);
				}
			}
		}

		queryForm.setAutoTrim(doTrim);
		queryForm.setPreparedParams(preparedParams);
		queryForm.setDoSum(doSum);

		QueryParam queryParam = queryForm.createQueryParm();
		QueryResult result = null;
		try {
			result = service.query(queryKey, queryParam);
		} catch (Exception e) {
			throw new QueryUIException("execute query fail, queryKey[" + queryKey + "] service[" + queryService + "] message:" + e.getMessage(), e);
		}
		queryForm.setQueryResult(result);

		long endTime = System.currentTimeMillis();
		if ((endTime - startTime) >= THRESHOLD)
			LOGGER.info("SlowQueryService: {} used {} ms to execute, you need to OPT. it.",
					queryService + "." + queryKey, endTime - startTime);
		return queryForm;
	}

}

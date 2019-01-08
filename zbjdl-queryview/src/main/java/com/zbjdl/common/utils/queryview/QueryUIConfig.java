/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview;

import com.zbjdl.common.utils.queryview.interceptor.ControllerContext;

/**
 * 查询组件的默认配置参数对象
 * 可以在spring里面定义一个该对象，覆盖默认配置(这个对象实现了ServletContextAware接口，只要在spring里面定义，它会自动放到ServletContext里面)
 *
 * 
 * @since：2012-5-20 下午07:35:26
 * @version:
 */
public class QueryUIConfig {

	public static final String QUERYUI_CONFIG = "_queryui_config";

	/**
	 * 默认的页大小
	 */
	private int pageSize = 20;

	/**
	 * 默认的所有查询UI标签样式名称
	 */
	private String baseCssClass = "list";

	/**
	 * 默认的查询服务名称
	 */
	private String queryService = "queryService";

	/**
	 * 默认的查询列表模板
	 */
	private String queryTableTemplate = "querytable";

	/**
	 * 默认的查询分页模板
	 */
	private String queryPagingTemplate = "querypaging";

	/**
	 * 可以指定一个table的class名称
	 */
	private String tableClass = "table table-striped table-hover table-condensed";

	public String getBaseCssClass(String _baseCssClass) {
		return _baseCssClass != null ? _baseCssClass : baseCssClass;
	}

	public int getPageSize(Integer _pageSize) {
		return _pageSize != null ? _pageSize : pageSize;
	}

	public String getQueryService(String _queryService) {
		return _queryService != null ? _queryService : queryService;
	}

	public String getQueryTableTemplate(String _queryTableTemplate) {
		return _queryTableTemplate != null ? _queryTableTemplate : queryTableTemplate;
	}

	public String getQueryPagingTemplate(String _queryPagingTemplate) {
		return _queryPagingTemplate != null ? _queryPagingTemplate : queryPagingTemplate;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String getBaseCssClass() {
		return baseCssClass;
	}

	public void setBaseCssClass(String baseCssClass) {
		this.baseCssClass = baseCssClass;
	}

	public String getQueryService() {
		return queryService;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setQueryService(String queryService) {
		this.queryService = queryService;
	}

	public String getTableClass() {
		return tableClass;
	}

	public void setTableClass(String tableClass) {
		this.tableClass = tableClass;
	}

	public static QueryUIConfig getConfiguration() {
		QueryUIConfig config = (QueryUIConfig) ControllerContext.getContext().getContextMap().get(QUERYUI_CONFIG);
		if (config == null) {
			config = new QueryUIConfig();
			ControllerContext.getContext().getContextMap().put(QUERYUI_CONFIG, config);
		}
		return config;
	}
}

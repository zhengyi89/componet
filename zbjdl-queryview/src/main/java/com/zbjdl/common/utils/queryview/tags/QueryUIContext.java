/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.CommonUtils;
import com.zbjdl.common.utils.MessageFormater;
import com.zbjdl.common.utils.StringUtils;
import com.zbjdl.common.utils.config.TextResource;
import com.zbjdl.common.utils.queryview.QueryForm;
import com.zbjdl.common.utils.queryview.QueryUIConfig;
import com.zbjdl.common.utils.queryview.QueryUIException;
import com.zbjdl.common.utils.queryview.tags.bean.ColumnBean;
import com.zbjdl.common.utils.queryview.tags.bean.PreparedParamBean;
import com.zbjdl.common.utils.queryview.tags.bean.RowBean;
import com.zbjdl.common.utils.queryview.tags.bean.RowStatus;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.jsp.PageContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>Title: 查询 UI 上下文</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-15 10:12
 */
public class QueryUIContext {

	public static final String ROWSTATUS = "_rowstatus";
	public static final String TEXT_RESOURCE = "_textResource";
	public static final String MESSAGE_FORMATER = "_messageFormater";
	public static final String ORDER_BY_MAP = "_ajax_orderby";
	public static final String ROOT_OBJECT = "rootObject";
	public static final String SUM_OBJECT = "sumObject";

	public static final String QUERYUI_CONTEXT_KEY = "_ajaxquery_context_key";

	private String queryKey;
	private String formId;
	private String tableAttrs = "";
	private String titleRowAttrs = "";
	private List<ColumnBean> titles;            //表头名称
	private List<String> sumTitles;            //sum表头名称
	private List<ColumnBean> sumListData;
	private List<ColumnBean> unionTitles;        //合并项的表头名称
	private List<RowBean> listData;
	private TagHelper helper;
	private List queryData;
	private Integer index;
	private Object currentValue;
	private boolean titieRowInitialized = false;        //表头是否初始化完毕
	private boolean titleCellInitialized = false;        //表行是否初始化完毕
	private boolean currentCellInitialized = false;
	private RowBean currentRow;
	private List<PreparedParamBean> preparedParams;
	private String baseCssClass = "queryui";
	private QueryForm queryForm;
	private String noDataMessage;
	private boolean titleHiding;
	private String queryUrl;
	private String contextUrl;
	private RowStatus rowStatus;
	private String titleRelation = "";
	private String showValueRelation="";

	private final TextResource textResource = new TextResource();

	private final MessageFormater messageFormater = new MessageFormater();

	/**
	 * 排序的字段
	 */
	private String orderby;

	/**
	 * true : 升序 ,false 降序
	 */
	private boolean orderByAsc;

	public QueryUIContext(TagHelper helper) {
		this.helper = helper;
	}

	public void addPreparedParam(PreparedParamBean bean) {
		if (preparedParams == null) {
			preparedParams = new ArrayList<PreparedParamBean>();
		}
		preparedParams.add(bean);
	}

	/**
	 * 添加表头
	 *
	 * @param content           表头内容
	 * @param dynamicAttributes 动态添加的HTML属性
	 */
	public void addTitleCell(String content, Map<String, Object> dynamicAttributes) {
		String union = (String) dynamicAttributes.get("union");
		if (StringUtils.isNotBlank(union)) {
			ColumnBean bean = findColumnBeanByUnion(union, titles);
			if (bean != null) {
				unionTitles.add(populateTitleColumnBean(content, dynamicAttributes));
			} else {
				titles.add(populateTitleColumnBean(content, dynamicAttributes));
			}
		} else {
			titles.add(populateTitleColumnBean(content, dynamicAttributes));
		}
		titleCellInitialized = true;
	}

	/**
	 * 添加表列
	 *
	 * @param value
	 * @param dynamicAttributes
	 */
	public void addCell(String value, Map<String, Object> dynamicAttributes) {
		String union = (String) dynamicAttributes.get("union");
		if (StringUtils.isNotBlank(union)) {
			ColumnBean bean = findColumnBeanByUnion(union, currentRow.getCells());
			if (bean != null) {
				currentRow.addUnionCell(populateCellColumnBean(value, dynamicAttributes));
			} else {
				currentRow.addCell(populateCellColumnBean(value, dynamicAttributes));
			}
		} else {
			currentRow.addCell(populateCellColumnBean(value, dynamicAttributes));
		}
		currentCellInitialized = true;
	}

	public void setTableAttrs(Map<String, Object> dynamicAttributes) {
		tableAttrs = retriveDynamicAttrs(dynamicAttributes, "table", true);
	}

	public void setRowAttrs(Map<String, Object> dynamicAttributes) {
		String rowparity = (index % 2) == 1 ? "odd" : "even";
		currentRow.setAttrs(retriveDynamicAttrs(dynamicAttributes, "tr_" + rowparity, true));
	}

	public void setTitleRowAttrs(Map<String, Object> dynamicAttributes) {
		titleRowAttrs = retriveDynamicAttrs(dynamicAttributes, "title_tr", false);
	}

	/**
	 * @param dynamicAttributes
	 * @param elementName
	 * @param hasDefaultCss     是否有默认的样式
	 * @return
	 */
	private String retriveDynamicAttrs(Map<String, Object> dynamicAttributes, String elementName, boolean hasDefaultCss) {
		StringBuilder htmlAttrs = new StringBuilder();
		boolean cssDefined = false;
		if (dynamicAttributes != null && dynamicAttributes.size() > 0) {
			for (Entry<String, Object> entry : dynamicAttributes.entrySet()) {
				String key = entry.getKey();
				if ("class".equalsIgnoreCase(key)) {
					cssDefined = true;
				}
				Object value = entry.getValue();
				String attr_value = value == null ? null : value.toString();
				attr_value = helper.findStringValue(attr_value);
				htmlAttrs.append(key).append("=\"").append(attr_value).append("\" ");
			}
		}
		if (!cssDefined) {
			QueryUIConfig config = QueryUIConfig.getConfiguration();
			if (config.getTableClass() != null && "table".equals(elementName)) {
				htmlAttrs.append("class=\"").append(config.getTableClass()).append("\"");
			} else if (hasDefaultCss) {
				htmlAttrs.append("class=\"").append(baseCssClass).append("_").append(elementName).append("\"");
			}
		}

		return htmlAttrs.toString();
	}

	public void prepareTitleRow() {
		titles = new ArrayList<ColumnBean>();
		unionTitles = new ArrayList<ColumnBean>();
	}

	public boolean prepareRow(PageContext pageContext) {
		if (listData == null) {
			listData = new ArrayList<RowBean>();
		}
		if (queryData == null) {
			return false;
		}
		if (index == null) {
			index = 0;
		} else {
			index++;
		}
		if (index >= queryData.size()) {
			return false;
		}
		currentValue = queryData.get(index);
		pageContext.setAttribute(ROOT_OBJECT, currentValue);
		pageContext.setAttribute(SUM_OBJECT, queryForm.getQueryResult().getSumData());
		
		currentRow = new RowBean();
		currentRow.setIndex(index);
		rowStatus.next();
		return true;
	}

	public void completeRow(PageContext pageContext) {
		pageContext.removeAttribute(ROOT_OBJECT);
		if (currentRow.size() != titles.size()) {
			throw new QueryUIException("title and data column not match!");
		}
		listData.add(currentRow);
		if (CheckUtils.isEmpty(currentRow.getAttrs())) {
			setRowAttrs(null);
		}
	}

	public void completeTitleRow(PageContext pageContext) {
		titieRowInitialized = true;
		if (CheckUtils.isEmpty(titleRowAttrs)) {
			setTitleRowAttrs(null);
		}
		rowStatus = new RowStatus(queryForm.getStratIndex());
		pageContext.setAttribute(ROWSTATUS, rowStatus);
		pageContext.setAttribute(TEXT_RESOURCE, textResource);
		pageContext.setAttribute(MESSAGE_FORMATER, messageFormater);
	}

	public boolean hasQueryData() {
		return (getQueryData() != null && getQueryData().size() > 0);
	}

	/**
	 * 添加order by键值对
	 *
	 * @param orderby
	 * @return
	 */
	public String addOrderByPair(String orderby) {
		if (StringUtils.isBlank(orderby))
			return null;
		if (orderby.startsWith("@")) {
			orderby = orderby.substring(1, orderby.length());
		}
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		Map<String, String> orderbyMap = (Map<String, String>) webApplicationContext.getServletContext().getAttribute(ORDER_BY_MAP);
		if (orderbyMap == null) {
			orderbyMap = new HashMap<String, String>();
			webApplicationContext.getServletContext().setAttribute(ORDER_BY_MAP, orderbyMap);
		}

		String key = null;
		for (Entry<String, String> entry : orderbyMap.entrySet()) {
			if (orderby.equals(entry.getValue()))
				key = entry.getKey();
		}
		if (key == null) {
			key = CommonUtils.getUUID();
			orderbyMap.put(key, orderby);
		}
		return key;
	}

	/**
	 * 根据union，查找columnBean
	 *
	 * @param union
	 * @return
	 */
	private ColumnBean findColumnBeanByUnion(String union, List<ColumnBean> columns) {
		for (ColumnBean column : columns) {
			if (union.equals(column.getUnion()))
				return column;
		}
		return null;
	}

	/**
	 * 构造 title ColumnBean
	 *
	 * @param content
	 * @param dynamicAttributes
	 * @return
	 */
	private ColumnBean populateTitleColumnBean(String content, Map<String, Object> dynamicAttributes) {
		ColumnBean col = new ColumnBean();
		col.setContent(content == null ? "" : content);
		String union = (String) dynamicAttributes.get("union");
		col.setUnion(union);
		if (StringUtils.isNotBlank(union))
			col.setUnionable(true);
		if ("true".equals(dynamicAttributes.get("sortable"))) {
			col.setSortable(true);
			col.setOrderby(addOrderByPair((String) dynamicAttributes.get("orderby")));
		}
		dynamicAttributes.remove("sortable");
		dynamicAttributes.remove("orderby");
		dynamicAttributes.remove("union");
		String attrs = retriveDynamicAttrs(dynamicAttributes, "th", false);
		col.setAttrs(attrs);
		return col;
	}

	/**
	 * 构造 表列的 ColumnBean
	 *
	 * @param content
	 * @param dynamicAttributes
	 * @return
	 */
	private ColumnBean populateCellColumnBean(String content, Map<String, Object> dynamicAttributes) {
		ColumnBean col = new ColumnBean();

		// 设置内容
		if (StringUtils.isNotBlank(content)) {
			boolean escapeHtml = (Boolean) dynamicAttributes.get("escapeHtml");
			boolean escapeJavaScript = (Boolean) dynamicAttributes.get("escapeJavaScript");
			boolean escapeXml = (Boolean) dynamicAttributes.get("escapeXml");
			boolean escapeCsv = (Boolean) dynamicAttributes.get("escapeCsv");
			if (escapeHtml) {
				content = StringEscapeUtils.escapeHtml4(content);
			}
			if (escapeJavaScript) {
				content = StringEscapeUtils.escapeEcmaScript(content);
			}
			if (escapeXml) {
				content = StringEscapeUtils.escapeXml(content);
			}
			if (escapeCsv) {
				content = StringEscapeUtils.escapeCsv(content);
			}
		} else {
			content = "";
		}
		col.setContent(content);

		String union = (String) dynamicAttributes.get("union");
		col.setUnion(union);
		if (StringUtils.isNotBlank(union))
			col.setUnionable(true);
		String spanClass = (String) dynamicAttributes.get("spanClass");
		if (StringUtils.isNotBlank(spanClass)) {
			spanClass = "class=\"" + spanClass + "\"";
		} else
			spanClass = "";
		col.setSpanClass(spanClass);

		dynamicAttributes.remove("sortable");
		dynamicAttributes.remove("orderby");
		dynamicAttributes.remove("union");
		dynamicAttributes.remove("spanClass");
		dynamicAttributes.remove("escapeHtml");
		dynamicAttributes.remove("escapeJavaScript");
		dynamicAttributes.remove("escapeXml");
		dynamicAttributes.remove("escapeCsv");
		String attrs = retriveDynamicAttrs(dynamicAttributes, "td", false);
		col.setAttrs(attrs);
		return col;
	}

	/**
	 * 添加order by键值对
	 *
	 * @param orderby
	 * @return
	 */
	public void addSumColumn(String cloumnName,ColumnBean columnBean) {
		
		if(this.sumTitles == null){
			sumTitles = new ArrayList<>();
		}
		if(this.sumListData == null){
			sumListData = new ArrayList<>();
		}
		if(!sumTitles.contains(cloumnName)){
			sumTitles.add(cloumnName);
			sumListData.add(columnBean);
		}else{
			return;
		}
		
	}
	public boolean isTitleHiding() {
		return titleHiding;
	}

	public void setTitleHiding(boolean titleHiding) {
		this.titleHiding = titleHiding;
	}

	public String getNoDataMessage() {
		return noDataMessage;
	}

	public void setNoDataMessage(String noDataMessage) {
		this.noDataMessage = noDataMessage;
	}

	public boolean isTitleRowInitialized() {
		return titieRowInitialized;
	}

	public void prepareTitleCell() {
		titleCellInitialized = false;
	}

	public boolean isTitleCellInitialized() {
		return titleCellInitialized;
	}

	public void prepareCell() {
		currentCellInitialized = false;
	}

	public boolean isCurrentCellInitialized() {
		return currentCellInitialized;
	}

	public String getBaseCssClass() {
		return baseCssClass;
	}

	public void setBaseCssClass(String baseCssClass) {
		this.baseCssClass = baseCssClass;
	}

	public String getQueryUrl() {
		return queryUrl;
	}

	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}

	public QueryForm getQueryForm() {
		return queryForm;
	}

	public void setQueryForm(QueryForm queryForm) {
		this.queryForm = queryForm;
		this.queryData = queryForm.getQueryData();
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public List getQueryData() {
		return this.queryData;
	}

	public void setFormId(String _formId) {
		this.formId = _formId;
	}

	public String getFormId() {
		return formId;
	}

	public String getTableAttrs() {
		return tableAttrs;
	}

	public String getTitleRowAttrs() {
		return titleRowAttrs;
	}

	public Integer getIndex() {
		return index;
	}

	public Object getCurrentValue() {
		return currentValue;
	}

	public List<ColumnBean> getTitles() {
		return titles;
	}

	public List<RowBean> getListData() {
		return listData;
	}

	public List<PreparedParamBean> getPreparedParams() {
		return preparedParams;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public boolean isOrderByAsc() {
		return orderByAsc;
	}

	public void setOrderByAsc(boolean orderByAsc) {
		this.orderByAsc = orderByAsc;
	}

	public List<ColumnBean> getUnionTitles() {
		return unionTitles;
	}

	public void setUnionTitles(List<ColumnBean> unionTitles) {
		this.unionTitles = unionTitles;
	}

	public String getTitleRelation() {
		String returnStr = "";
		for (ColumnBean title: titles){
			returnStr+=title.getContent()+",";
		}
		return titleRelation;
	}

	public void setTitleRelation(String titleRelation) {
		this.titleRelation = titleRelation;
	}


	public List<ColumnBean> getSumListData() {
		return sumListData;
	}

	public void setSumListData(List<ColumnBean> sumListData) {
		this.sumListData = sumListData;
	}

	public String getContextUrl() {
		return contextUrl;
	}

	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}

	public String getShowValueRelation() {
		return showValueRelation;
	}

	public void setShowValueRelation(String showValueRelation) {
		this.showValueRelation = showValueRelation;
	}


}

/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.queryview.*;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p>Title: 查询表格标签</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-15 10:12
 */
public class QueryTableTag extends QueryUITagSupport {

	protected static final Logger LOGGER = LoggerFactory.getLogger(QueryTableTag.class);

	public static final String QUERY_TABLA = "_query_table_";
	public static final String PAGING_POSITION_TOP = "top";
	public static final String PAGING_POSITION_BOTH = "both";

	private String formId;
	private String queryUrl;
	private String queryForm;
	private String paging;
	private String pagingPosition;
	private String queryKey;
	private String queryService;
	private String pageSize;
	private String doSum;
	private String autoTrim;
	private String baseCssClass;
	private String template;
	private String pagingTemplate;
	private String titleHiding;
	private boolean showExpButton;
	private String contextUrl;

	/**
	 * 第一次请求
	 *
	 * @return
	 */
	private boolean isFirstQueryInPage() {
		return null == pageContext.getAttribute(QUERY_TABLA);
	}

	private void setQueryInPage() {
		pageContext.setAttribute(QUERY_TABLA, new Object());
	}

	/**
	 * doStartTag()方法是遇到标签开始时会呼叫的方法，
	 * 其合法的返回值是EVAL_BODY_INCLUDE与SKIP_BODY,
	 * 前者表示将显示标签间的文字，
	 * 后者表示不显示标签间的文字
	 *
	 * @return
	 * @throws JspException
	 */
	@Override
	public int doStartTag() throws JspException {
		QueryUIContext context = this.initContext();
		context.prepareTitleRow();
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * doAfterBody(),这个方法是在显示完标签间文字之后呼叫的，
	 * 其返回值有EVAL_BODY_AGAIN与SKIP_BODY，
	 * 前者会再显示一次标签间的文字，
	 * 后者则继续执行标签处理的下一步。
	 *
	 * @return
	 * @throws JspException
	 */
	@Override
	public int doAfterBody() throws JspException {
		QueryUIContext context = this.getContext();
		if (context.isTitleRowInitialized()) {
			context.completeRow(pageContext);
		} else {
			populateQueryUIContext(context);
			context.completeTitleRow(pageContext);
		}

		// 初始化上下文供列标签使用
		if (context.prepareRow(pageContext)) {
			Map<Object, Object> contextMap = (Map<Object, Object>) pageContext.getAttribute("rootObject");
			for (Map.Entry entry : contextMap.entrySet()) {
				pageContext.setAttribute((String) entry.getKey(), entry.getValue());
			}
			Map<Object, Object> sumContextMap = (Map<Object, Object>) pageContext.getAttribute("sumObject");
			if(sumContextMap != null && sumContextMap.size()>0){
				for (Map.Entry entry : sumContextMap.entrySet()) {
					pageContext.setAttribute((String) entry.getKey(), entry.getValue());
				}
			}

			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	/**
	 * doEndTag()方法是在遇到标签结束时呼叫的方法，
	 * 其合法的返回值是EVAL_PAGE与SKIP_PAGE，
	 * 前者表示处理完标签后继续执行以下的JSP网页，
	 * 后者是表示不处理接下来的JSP网页
	 *
	 * @return
	 * @throws JspException
	 */
	@Override
	public int doEndTag() throws JspException {
		
		
		if (isFirstQueryInPage()) {
			write(StaticResource.getStaticHtml("querytable.js"));
			setQueryInPage();
		}
		QueryUIConfig config = QueryUIConfig.getConfiguration();
		QueryUIContext context = this.getContext();
		Map vars = createTemplateContextMap();
		context.setContextUrl(contextUrl);
		
		//Add By:MengXC 2016/2/26 19:54 表头上方增加导出Excel按钮
		if(showExpButton){
			String buttonOut = helper.evalTemplate("exportbutton", vars);
			write(buttonOut);
		}
//2016-09-13 注释掉 <q:nodata/>标签的值得输出 ,将值放到querytable模板中输出，从而可以自定义
		boolean titleHidding_attr = false;
		if (!context.hasQueryData()) {
			if (titleHiding != null) {
				titleHidding_attr = this.findBooleanValue(titleHiding);
			} else if (context.getNoDataMessage() != null) {
				titleHidding_attr = true;
			}
//			if (context.getNoDataMessage() != null) {
//				write(context.getNoDataMessage());
//			}
			context.setTitleHiding(titleHidding_attr);
		}

		if (formId == null && !this.isFormCreated(context.getQueryKey())) {
			String form_out = helper.evalTemplate("queryform", vars);
			write(form_out);
			this.setFormCreated(context.getQueryKey());
		}

		boolean pagingTop = false;
		boolean pagingBottom = false;
		boolean paging_attr = true;
		if (paging != null) {
			paging_attr = this.findBooleanValue(paging);
		}
		String paging_out = null;
		if (paging_attr) {
			paging_out = helper.evalTemplate(config.getQueryPagingTemplate(findStringValue(pagingTemplate)), vars);
			String position_attr = this.findStringValue(pagingPosition);
			if (PAGING_POSITION_BOTH.equals(position_attr)) {
				pagingTop = true;
				pagingBottom = true;
			} else if (PAGING_POSITION_TOP.equals(position_attr)) {
				pagingTop = true;
			} else {
				pagingBottom = true;
			}
		}

		if (pagingTop) {
			write(paging_out);
		}

		String table_out = helper.evalTemplate(config.getQueryTableTemplate(findStringValue(template)), vars);
		write(table_out);

		if (pagingBottom) {
			write(paging_out);
		}
		clearContext();
		return EVAL_PAGE;
	}

	/**
	 * 填充 Context
	 *
	 * @param context
	 */
	public void populateQueryUIContext(QueryUIContext context) {
		QueryUIConfig config = QueryUIConfig.getConfiguration();
		QueryForm queryForm_attr = null;
		if (queryForm != null) {
			Object obj = this.findObjectValue(queryForm);
			if (obj == null) {
				throw new QueryUIException("queryForm obj must not null : " + queryForm);
			}
			if (!(obj instanceof QueryForm)) {
				throw new QueryUIException("queryForm obj must be instanceof QueryForm : " + obj);
			}
			queryForm_attr = (QueryForm) obj;
		} else if (queryKey != null) {
			String queryKey_attr = this.findStringValue(queryKey);
			String queryService_attr = this.findStringValue(queryService);
			Integer pageSize_attr = this.findIntegerValue(pageSize);
			Boolean doSum_attr = this.findBooleanValue(doSum);
			Boolean autoTrim_attr = this.findBooleanValue(autoTrim);
			queryForm_attr = QueryExecutor.executeQuery(queryKey_attr, config.getQueryService(queryService_attr), config.getPageSize(pageSize_attr), doSum_attr, autoTrim_attr, context.getPreparedParams());
		} else {
			throw new QueryUIException("queryForm or queryKey must be specified");
		}

		String _queryKey = queryForm_attr.getQueryKey();
		if (CheckUtils.isEmpty(_queryKey)) {
			throw new QueryUIException("queryKey attribute must be not null : " + _queryKey);
		}

		String formId_attr = null;
		String queryUrl_attr = null;
		if (formId != null) {
			formId_attr = this.findNoEmptyStringValue(formId);
		} else {
			formId_attr = generateFormId(_queryKey);
			if (queryUrl == null) {
				queryUrl_attr = "";
			} else {
				queryUrl_attr = this.findNoEmptyStringValue(queryUrl);
			}
		}

		context.setQueryKey(_queryKey);
		context.setFormId(formId_attr);
		context.setQueryUrl(queryUrl_attr);
		context.setQueryForm(queryForm_attr);
		context.setBaseCssClass(config.getBaseCssClass(this.findStringValue(baseCssClass)));
		context.setTableAttrs(dynamicAttributes);
		context.getTitles();
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public void setQueryService(String queryService) {
		this.queryService = queryService;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public void setQueryForm(String queryForm) {
		this.queryForm = queryForm;
	}

	public void setPaging(String paging) {
		this.paging = paging;
	}

	public void setPagingPosition(String pagingPosition) {
		this.pagingPosition = pagingPosition;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setPagingTemplate(String pagingTemplate) {
		this.pagingTemplate = pagingTemplate;
	}

	public void setBaseCssClass(String baseCssClass) {
		this.baseCssClass = baseCssClass;
	}

	public void setTitleHiding(String titleHiding) {
		this.titleHiding = titleHiding;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public void setDoSum(String doSum) {
		this.doSum = doSum;
	}

	public void setAutoTrim(String autoTrim) {
		this.autoTrim = autoTrim;
	}

	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
	public void setShowExpButton(boolean showExpButton) {
		this.showExpButton = showExpButton;
	}
	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}

}

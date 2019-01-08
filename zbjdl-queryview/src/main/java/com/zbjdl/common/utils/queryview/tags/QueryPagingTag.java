/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.queryview.QueryForm;
import com.zbjdl.common.utils.queryview.QueryUIConfig;
import com.zbjdl.common.utils.queryview.QueryUIException;

import javax.servlet.jsp.JspException;

import java.util.Map;

/**
 * <p>Title: 查询分页标签</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-16 18:27
 */
public class QueryPagingTag extends QueryUITagSupport {

	private String queryForm;
	private String baseCssClass;
	private String template;
	private String formId;
	private String queryUrl;

	public int doStartTag() throws JspException {
		QueryUIContext context = this.initContext();
		QueryUIConfig config = QueryUIConfig.getConfiguration();

		Object obj = this.findObjectValue(queryForm);
		if (obj == null || !(obj instanceof QueryForm)) {
			throw new QueryUIException("queryForm obj must not null and instanceof QueryForm : " + obj);
		}
		QueryForm queryForm_attr = (QueryForm) obj;

		String _queryKey = queryForm_attr.getQueryKey();
		if (CheckUtils.isEmpty(_queryKey)) {
			throw new QueryUIException("queryKey attribute must be not null : " + _queryKey);
		}

		String formId_attr = null;
		String queryUrl_attr = null;
		boolean autoCreateForm = false;
		if (formId != null) {
			formId_attr = this.findNoEmptyStringValue(formId);
		} else {
			formId_attr = generateFormId(_queryKey);
			if (queryUrl == null) {
				queryUrl_attr = "";
			} else {
				queryUrl_attr = this.findNoEmptyStringValue(queryUrl);
			}
			autoCreateForm = true;
		}

		context.setQueryForm(queryForm_attr);
		context.setQueryKey(_queryKey);
		context.setFormId(formId_attr);
		context.setQueryUrl(queryUrl_attr);
		context.setBaseCssClass(config.getBaseCssClass(baseCssClass));

		Map vars = this.createTemplateContextMap();

		if (autoCreateForm && !this.isFormCreated(_queryKey)) {
			String form_out = helper.evalTemplate("queryform", vars);
			write(form_out);
			this.setFormCreated(_queryKey);
		}
		String paging_out = helper.evalTemplate(config.getQueryPagingTemplate(findStringValue(template)), vars);
		write(paging_out);
		return SKIP_BODY;
	}

	public void setQueryForm(String queryForm) {
		this.queryForm = queryForm;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setBaseCssClass(String baseCssClass) {
		this.baseCssClass = baseCssClass;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
}

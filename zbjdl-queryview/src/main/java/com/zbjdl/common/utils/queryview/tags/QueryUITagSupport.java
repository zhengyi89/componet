package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.queryview.QueryUIException;
import com.zbjdl.common.utils.queryview.interceptor.ControllerContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: 描述</p>
 *
 */
public abstract class QueryUITagSupport extends BodyTagSupport implements DynamicAttributes {

	protected static final Logger LOGGER = LoggerFactory.getLogger(QueryUITagSupport.class);

	protected static TagHelper helper = new TagHelper("com/zbjdl/common/utils/queryview/");

	protected Map<String, Object> dynamicAttributes = new HashMap<String, Object>();

	public static final String QUERYUI_CONTEXT_KEY = "_ajaxquery_context_key";
	public static final String QUERYUI_QUERYFORM_KEY = "_ajaxquery_autocreate_form";

//	private Configuration freemarkerConfiguration;
//	private static final String DEFAULT_ENCODING = "utf-8";

	public QueryUIContext initContext() {
		QueryUIContext queryUIContext = new QueryUIContext(helper);
		ControllerContext.getContext().put(QUERYUI_CONTEXT_KEY, queryUIContext);
		return queryUIContext;
	}

	public QueryUIContext getContext() {
		return (QueryUIContext) ControllerContext.getContext().get( QUERYUI_CONTEXT_KEY);
	}

	public void clearContext() {
		pageContext.removeAttribute(QueryUIContext.ROWSTATUS);
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		if (!isValidDynamicAttribute(localName, value)) {
			throw new IllegalArgumentException(
					"Attribute " + localName + "=\"" + value + "\" is not allowed");
		}
		dynamicAttributes.put(localName, value);
	}

	protected boolean isValidDynamicAttribute(String localName, Object value) {
		return true;
	}

	public boolean isFormCreated(String queryKey) {
		return null != pageContext.getAttribute(QUERYUI_QUERYFORM_KEY + "_" + queryKey);
	}

	public void setFormCreated(String queryKey) {
		pageContext.setAttribute(QUERYUI_QUERYFORM_KEY + "_" + queryKey, new Object());
	}

	public String generateFormId(String queryKey) {
		return QUERYUI_QUERYFORM_KEY + "_" + queryKey;
	}

	public Object eval(String expr) {
		return helper.eval(expr);
	}

	public String findStringValue(String expr) {
		return helper.findStringValue(expr);
	}

	public Map createTemplateContextMap() {
		QueryUIContext context = this.getContext();
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("context", context);
		vars.put("helper", helper);
		return vars;
	}

	public String findNoEmptyStringValue(String expr) {
		String str = helper.findStringValue(expr);
		if (CheckUtils.isEmpty(expr)) {
			throw new QueryUIException("can't find attribute in context : " + expr);
		}
		return str;
	}

	public boolean findBooleanValue(String expr) {
		return helper.findBooleanValue(expr);
	}

	public Integer findIntegerValue(String expr) {
		return helper.findIntegerValue(expr);
	}

	public Long findLongValue(String expr) {
		return helper.findLongValue(expr);
	}

	public Object findObjectValue(String expr) {
		return helper.findObjectValue(expr);
	}

	public void write(String str) {
		if (str == null) {
			return;
		}
		try {
			getPreviousOut().write(str);
		} catch (IOException e) {
			throw new RuntimeException("write content error", e);
		}
	}

}

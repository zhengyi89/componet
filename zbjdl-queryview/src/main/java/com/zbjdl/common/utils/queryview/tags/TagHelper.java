package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.mvel.MVELTemplateFactory;
import com.zbjdl.common.mvel.MVELUtils;
import com.zbjdl.common.utils.queryview.interceptor.ControllerContext;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-17 14:16
 */
public class TagHelper {

	private static final String BOOLEAN_TRUE = "true";
	private static final String TAG_HELPER = "_helper";

	private MVELTemplateFactory templateFactory;

	public TagHelper(String templatePackage) {
		templateFactory = MVELTemplateFactory.createClassPathTemplateFactory(templatePackage);
	}

	public String escape(String str) {
		if (str == null) {
			return null;
		}
		return StringEscapeUtils.escapeHtml(str);
	}

	public Object eval(String expr) {
		Map contextMap = ControllerContext.getContext().getContextMap();
		return MVELUtils.eval(expr, contextMap.get("rootObject"), contextMap);
	}

	public String findStringValue(String expr) {
		if (expr == null) {
			return null;
		}
		TagExpression te = TagExpression.parseTagExpression(expr);
		if (te.isExpression()) {
			Object obj = eval(te.getValue());
			return obj == null ? null : obj.toString();
		} else {
			return te.getValue();
		}
	}

	public boolean findBooleanValue(String expr) {
		if (expr == null) {
			return false;
		}

		TagExpression te = TagExpression.parseTagExpression(expr);
		if (te.isExpression()) {
			return Boolean.TRUE.equals(eval(te.getValue()));
		} else {
			return BOOLEAN_TRUE.equals(te.getValue());
		}
	}

	public Long findLongValue(String expr) {
		if (expr == null) {
			return null;
		}
		TagExpression te = TagExpression.parseTagExpression(expr);
		if (te.isExpression()) {
			return (Long) eval(te.getValue());
		} else {
			return new Long(te.getValue());
		}
	}

	public Integer findIntegerValue(String expr) {
		if (expr == null) {
			return null;
		}
		TagExpression te = TagExpression.parseTagExpression(expr);
		if (te.isExpression()) {
			return (Integer) eval(te.getValue());
		} else {
			return new Integer(te.getValue());
		}
	}

	public Object findObjectValue(String expr) {
		if (expr == null) {
			return null;
		}

		TagExpression te = TagExpression.parseTagExpression(expr);
		if (te.isExpression()) {
			return eval(te.getValue());
		} else {
			return te.getValue();
		}
	}

	public String evalTemplate(String templateName, Map context) {
		context.put(TAG_HELPER, this);
		return templateFactory.evalTemplate(templateName, null, context);
	}

}

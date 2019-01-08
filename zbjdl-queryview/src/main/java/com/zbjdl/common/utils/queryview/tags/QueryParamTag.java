/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.utils.queryview.QueryUIException;
import com.zbjdl.common.utils.queryview.tags.bean.PreparedParamBean;

import javax.servlet.jsp.JspException;

/**
 * <p>Title: 查询参数标签</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-16 18:28
 */
public class QueryParamTag extends QueryUITagSupport {

	private static final long serialVersionUID = 340563195094449857L;

	private String name;
	private String value;
	private String preferred;

	public int doStartTag() throws JspException {
		if (findAncestorWithClass(this, QueryTableTag.class) == null) {
			throw new QueryUIException("QueryParamTag must be put in a QueryTableTag!");
		}
		QueryUIContext context = this.getContext();
		if (!context.isTitleRowInitialized()) {
			String name_attr = this.findStringValue(name);
			Object value_attr = this.findObjectValue(value);
			Boolean preferred_attr = this.findBooleanValue(preferred);
			context.addPreparedParam(new PreparedParamBean(name_attr, value_attr, preferred_attr));
		}
		return SKIP_BODY;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setPreferred(String preferred) {
		this.preferred = preferred;
	}

}

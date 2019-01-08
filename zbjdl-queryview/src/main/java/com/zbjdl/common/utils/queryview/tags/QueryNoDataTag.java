/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

import javax.servlet.jsp.JspException;

/**
 * <p>Title: 查询表格标签</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-16 18:26
 */
public class QueryNoDataTag extends QueryUITagSupport {

	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspException {
		QueryUIContext context = this.getContext();
		if (!context.hasQueryData()) {
			context.setNoDataMessage(getBodyContent().getString());

			try {
				getBodyContent().clearBody();
			} catch (Exception e) {
				// do nothing
			}
		}
		return EVAL_PAGE;
	}

}

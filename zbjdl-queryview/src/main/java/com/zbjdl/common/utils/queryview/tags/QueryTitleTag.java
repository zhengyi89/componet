/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.utils.queryview.QueryUIException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * <p>Title: 查询标题标签</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-15 10:12
 */
public class QueryTitleTag extends QueryUITagSupport {

	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspException {
		QueryUIContext context = this.getContext();
		if (!context.isTitleRowInitialized() && !context.isTitleCellInitialized() && getBodyContent() != null) {
			Tag parent = findAncestorWithClass(this, QueryColumnTag.class);
			if (parent == null) {
				throw new QueryUIException("QueryTitleTag must be put in a QueryColumnTag!");
			}
			context.addTitleCell(getBodyContent().getString(), dynamicAttributes);
		}
		return EVAL_PAGE;
	}

}

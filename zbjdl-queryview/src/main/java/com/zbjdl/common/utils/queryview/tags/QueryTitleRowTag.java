/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;


import com.zbjdl.common.utils.queryview.QueryUIException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * <p>Title: 查询标题列标签</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-16 18:29
 */
public class QueryTitleRowTag extends QueryUITagSupport {

	public int doStartTag() throws JspException {
		Tag parent = findAncestorWithClass(this, QueryTableTag.class);
		if (parent == null) {
			throw new QueryUIException("QueryRowTag must be put in a QueryTableTag!");
		}

		QueryUIContext context = this.getContext();
		if (!context.isTitleRowInitialized()) {
			context.setTitleRowAttrs(dynamicAttributes);
		}
		return SKIP_BODY;
	}

}

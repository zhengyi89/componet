/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.utils.queryview.QueryUIException;
import com.zbjdl.common.utils.queryview.tags.bean.ColumnBean;

import java.util.Map;

import javax.servlet.jsp.JspException;

/**
 * <p>Title: 查询结果列标签</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-16 18:30
 */
public class QuerySumTag extends QueryUITagSupport {

	private static final long serialVersionUID = 8516083104719200334L;

	private String name;
	private String title;
	private String unit;

	private String value;

	@Override
	public int doStartTag() throws JspException {
		if (findAncestorWithClass(this, QueryTableTag.class) == null) {
			throw new QueryUIException("QuerySumTag must be put in a QueryTableTag!");
		}
//		QueryUIContext context = this.getContext();
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		QueryUIContext context = this.getContext();
//		if (context.isTitleRowInitialized()) {
//			if (!context.isCurrentCellInitialized() && getBodyContent() != null) {
//				context.addCell(getBodyContent().getString(), dynamicAttributes);
//			}
//			if (!context.isCurrentCellInitialized()) {
//				throw new QueryUIException("column value or column content must be specified");
//			}
//		} else {
//			if (!context.isTitleCellInitialized()) {
//				throw new QueryUIException("title value or title tag must be specified");
//			}
//		}
		if(context.getQueryForm()!=null){
			//如果value传入值，则直接取value值即可，忽略name属性
			//如果没有value传入值，则按照name属性取值
			if(value != null){
				ColumnBean columnBean = new ColumnBean();
				columnBean.setAttrs((title!=null?title:""));
				columnBean.setContent(value);
				columnBean.setUnion(unit!=null?unit:"");
				context.addSumColumn(name,columnBean);
			}else{
				Map sumMap = context.getQueryForm().getQueryResult().getSumData();
				if(sumMap!=null){
					ColumnBean columnBean = new ColumnBean();
					columnBean.setAttrs((title!=null?title:""));
					columnBean.setContent(sumMap.get(name)!=null?sumMap.get(name).toString():"");
					columnBean.setUnion(unit!=null?unit:"");
					context.addSumColumn(name,columnBean);
				}

			}

		}
		return EVAL_PAGE;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



}

/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

import com.zbjdl.common.utils.queryview.QueryUIException;

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
public class QueryColumnTag extends QueryUITagSupport {

	private static final long serialVersionUID = 8516083104719200334L;

	private String value;
	private String title;
	private String width;
	private String sortable;
	private String orderby;
	private String union;
	private String dataIndex;
	private String showValueIndex;

	private boolean escapeHtml = true;
	private boolean escapeJavaScript = false;
	private boolean escapeXml = false;
	private boolean escapeCsv = false;

	@Override
	public int doStartTag() throws JspException {
	
		if (findAncestorWithClass(this, QueryTableTag.class) == null) {
			throw new QueryUIException("QueryColumnTag must be put in a QueryTableTag!");
		}
		QueryUIContext context = this.getContext();
		dynamicAttributes.put("escapeHtml", escapeHtml);
		dynamicAttributes.put("escapeJavaScript", escapeJavaScript);
		dynamicAttributes.put("escapeXml", escapeXml);
		dynamicAttributes.put("escapeCsv", escapeCsv);
		if (context.isTitleRowInitialized()) {
			context.prepareCell();
			if (value != null) {
				context.addCell(value, dynamicAttributes);
				return SKIP_BODY;
			}
		} else {
			context.prepareTitleCell();
			if (title != null) {
				if (orderby == null) {
					dynamicAttributes.put("orderby", value);
				}
				context.addTitleCell(title, dynamicAttributes);
				return SKIP_BODY;
			}
			return EVAL_BODY_BUFFERED;
		}
		return EVAL_BODY_BUFFERED;
	}

//	@Override
//	public void doInitBody() throws JspException {
//		Map<Object, Object> contextMap = (Map<Object, Object>) pageContext.getAttribute("rootObject");
//		for (Map.Entry entry : contextMap.entrySet()) {
//			pageContext.setAttribute((String) entry.getKey(), entry.getValue());
//		}
//		super.doInitBody();
//	}

	@Override
	public int doEndTag() throws JspException {
		QueryUIContext context = this.getContext();
		if (context.isTitleRowInitialized()) {
			if (!context.isCurrentCellInitialized() && getBodyContent() != null) {
				context.addCell(getBodyContent().getString(), dynamicAttributes);
			}
			if (!context.isCurrentCellInitialized()) {
				throw new QueryUIException("column value or column content must be specified");
			}
		} else {
			if (!context.isTitleCellInitialized()) {
				throw new QueryUIException("title value or title tag must be specified");
			}
			String relation = context.getTitleRelation();
			if(dataIndex != null){
				relation+=dataIndex+":"+title+",";
			}
			context.setTitleRelation(relation);
			
			String valueRelation = context.getShowValueRelation();
			if(dataIndex !=null && showValueIndex != null){
				valueRelation+= dataIndex+":"+showValueIndex+",";
			}
			context.setShowValueRelation(valueRelation);
		}
		return EVAL_PAGE;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setWidth(String width) {
		dynamicAttributes.put("width", width);
	}

	public void setSortable(String sortable) {
		dynamicAttributes.put("sortable", sortable);
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
		dynamicAttributes.put("orderby", orderby);
	}

	public void setUnion(String union) {
		dynamicAttributes.put("union", union);
	}

	public void setEscapeHtml(boolean escapeHtml) {
		this.escapeHtml = escapeHtml;
	}

	public void setEscapeJavaScript(boolean escapeJavaScript) {
		this.escapeJavaScript = escapeJavaScript;
	}

	public void setEscapeXml(boolean escapeXml) {
		this.escapeXml = escapeXml;
	}

	public void setEscapeCsv(boolean escapeCsv) {
		this.escapeCsv = escapeCsv;
	}

	public String getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}

	public String getShowValueIndex() {
		return showValueIndex;
	}

	public void setShowValueIndex(String showValueIndex) {
		this.showValueIndex = showValueIndex;
	}

	

}

/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags.bean;

/**
 * 表格行中的一列
 *
 * 
 * @since：2012-5-19 下午09:23:51
 * @version:
 */
public class ColumnBean {

	private String attrs = "";

	private String content;

	/**
	 * 合并项，如2列union相同，则2列合并1列显示
	 */
	private String union;

	/**
	 * 是否是 合并项
	 */
	private boolean unionable;

	/**
	 * 是否可排序
	 */
	private boolean sortable;

	/**
	 * 指定排序的列号
	 */
	private String orderby;

	/**
	 * 指定 TR里面的 SPAN 样式
	 */
	private String spanClass;

	public ColumnBean() {
	}

	public ColumnBean(String _content, String _attrs) {
		this.content = _content;
		this.attrs = _attrs;
	}

	/**
	 * 是否是当前的排序项
	 *
	 * @param currentOrderBy
	 */
	public boolean isCurrentSortColumn(String currentOrderBy) {
		return sortable && orderby != null && orderby.equals(currentOrderBy);
	}

	public String getAttrs() {
		return attrs;
	}

	public String getContent() {
		return content;
	}

	public String getUnion() {
		return union;
	}

	public void setUnion(String union) {
		this.union = union;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getSpanClass() {
		return spanClass;
	}

	public void setSpanClass(String spanClass) {
		this.spanClass = spanClass;
	}

	public boolean getUnionable() {
		return unionable;
	}

	public void setUnionable(boolean unionable) {
		this.unionable = unionable;
	}

}

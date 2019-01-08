/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格中的一行
 *
 * 
 * @since：2012-5-20 下午01:04:21
 * @version:
 */
public class RowBean {

	private List<ColumnBean> cells;
	private List<ColumnBean> unionCells;        //合并项的cells名称
	private String attrs = "";
	private int index;

	public RowBean() {
		cells = new ArrayList<ColumnBean>();
		unionCells = new ArrayList<ColumnBean>();
	}

	public int size() {
		return cells.size();
	}

	public void addCell(ColumnBean cell) {
		cells.add(cell);
	}

	public void addUnionCell(ColumnBean cell) {
		unionCells.add(cell);
	}

	public void setCells(List<ColumnBean> cells) {
		this.cells = cells;
	}

	public List<ColumnBean> getCells() {
		return cells;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<ColumnBean> getUnionCells() {
		return unionCells;
	}

	public void setUnionCells(List<ColumnBean> unionCells) {
		this.unionCells = unionCells;
	}
}

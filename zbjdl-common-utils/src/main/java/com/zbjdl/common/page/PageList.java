package com.zbjdl.common.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * 包含“分页”信息的<code>List</code>。
 *
 */
public class PageList implements Iterable<Object>, Serializable {

	private static final long serialVersionUID = 5906686481839066126L;

	private Paginator paginator;

	private List<Object> data;

	/**
	 * 创建一个<code>PageList</code>。
	 */
	public PageList() {
		paginator = new Paginator();
		data = new ArrayList<Object>();
	}

	/**
	 * 创建<code>PageList</code>，并将指定<code>Collection</code>中的内容复制到新的list中。
	 *
	 * @param c
	 *            要复制的<code>Collection</code>
	 */
	public PageList(Collection<Object> c) {
		this(c, null);
	}

	/**
	 * 创建<code>PageList</code>，并将指定<code>Collection</code>中的内容复制到新的list中。
	 *
	 * @param c
	 *            要复制的<code>Collection</code>
	 */
	public PageList(Collection<Object> c, Paginator paginator) {
		this.paginator = (paginator == null) ? new Paginator() : paginator;
		data = new ArrayList<Object>();
		if (c != null) {
			data.addAll(c);
		}
	}

	/**
	 * 设置分页器。
	 *
	 * @param paginator
	 *            要设置的分页器对象
	 */
	public void setPaginator(Paginator paginator) {
		if (paginator != null) {
			this.paginator = paginator;
		}
	}

	public Object get(int index) {
		if (index < 0) {
			index = 0;
		}
		return data.get(index);
	}

	/**
	 * 为使用通用序列化，返回总size，值永远不小于1
	 */
	public int size() {
		return data.size();
	}

	public boolean add(Object e) {
		if (e != null && e.getClass() == Paginator.class) {
			setPaginator((Paginator) e);
		} else {
			data.add(e);
		}
		return true;
	}

	public Object[] toArray() {
		return data.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	/**
	 * 取得分页器，可从中取得有关分页和页码的所有信息。
	 *
	 * @return 分页器对象
	 */
	public Paginator getPaginator() {
		return this.paginator;
	}

	public Iterator<Object> iterator() {
		return data.iterator();
	}

	public ListIterator<Object> listIterator() {
		return data.listIterator();
	}

	public ListIterator<Object> listIterator(int index) {
		return data.listIterator(index);
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		if (data != null) {
			this.data = data;
		} else {
			this.data.clear();
		}
	}

	public String toString() {
		return this.paginator + " data : " + data.toString();
	}
}

package com.zbjdl.common.utils.ftp;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ftp文件
 * 
 */
public class FtpFile implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 详细信息
	 */
	private String detailInfo;
	/**
	 * 是否目录
	 */
	private boolean dir;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	
	
	/**
	 * 文件大小
	 * */
	private long size;
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * name
	 * 
	 * @return the name
	 */

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * dir
	 * 
	 * @return the dir
	 */

	public boolean isDir() {
		return dir;
	}

	/**
	 * @param dir
	 *            the dir to set
	 */
	public void setDir(boolean dir) {
		this.dir = dir;
	}

	/**
	 * addTime
	 * 
	 * @return the addTime
	 */

	public Date getAddTime() {
		return addTime;
	}

	/**
	 * @param addTime
	 *            the addTime to set
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * modifyTime
	 * 
	 * @return the modifyTime
	 */

	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime
	 *            the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * detailInfo
	 * 
	 * @return the detailInfo
	 */

	public String getDetailInfo() {
		return detailInfo;
	}

	/**
	 * @param detailInfo
	 *            the detailInfo to set
	 */
	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	
}

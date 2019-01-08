//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore;

import com.zbjdl.common.utils.event.model.BaseModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class QuickLocation extends BaseModel {
    private static final long serialVersionUID = 3563457093575455486L;
    private static final transient String PROTOCOL = "filedb://";
    private String tableName;
    private String fileName;
    private String key;
    private long gmtModify;
    private int errCount = 0;
    private int status;
    private long keyOffset;
    private long offset;
    private int length;

    public QuickLocation(String tableName, String fileName, String key, long gmtModify, long offset, int length, long keyOffset, int errCount) {
        this.tableName = tableName;
        this.fileName = fileName;
        this.key = key;
        this.gmtModify = gmtModify;
        this.offset = offset;
        this.length = length;
        this.keyOffset = keyOffset;
        this.errCount = errCount;
    }

    public static QuickLocation fromURL(String url) {
        if (StringUtils.isNotBlank(url) && url.startsWith("filedb://")) {
            url = url.substring("filedb://".length());
            String[] pas = StringUtils.split(url, "/");
            if (null != pas) {
                if (pas.length == 8) {
                    QuickLocation q = new QuickLocation(pas[0], pas[1], pas[2], NumberUtils.toLong(pas[3]), System.currentTimeMillis(), NumberUtils.toInt(pas[5]), NumberUtils.toLong(pas[6]), NumberUtils.toInt(pas[7]));
                    q.setStatus(NumberUtils.toInt(pas[4]));
                    return q;
                }

                if (pas.length == 4) {
                    return new QuickLocation(pas[0], pas[1], pas[2], NumberUtils.toLong(pas[3]), -1L, -1, -1L, 0);
                }
            }
        }

        return null;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getKey() {
        return this.key;
    }

    public void setGmtModify(long gmtModify) {
        this.gmtModify = gmtModify;
    }

    public long getGmtModify() {
        return this.gmtModify;
    }

    public int getErrCount() {
        return this.errCount;
    }

    public void setErrCount(int errCount) {
        this.errCount = errCount;
    }

    public long getKeyOffset() {
        return this.keyOffset;
    }

    public long getOffset() {
        return this.offset;
    }

    public int getLength() {
        return this.length;
    }

    public String toURL() {
        return (new StringBuilder(64)).append("filedb://").append(this.tableName).append("/").append(this.fileName).append("/").append(this.key).append("/").append(this.status).append("/").append(this.offset).append("/").append(this.length).append("/").append(this.keyOffset).append("/").append(this.errCount).toString();
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.client.model;

import com.zbjdl.common.utils.event.EventBase;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Notify extends EventBase implements Serializable {
    private static final long serialVersionUID = -5818437260636301071L;
    private long gmtExpire;
    private String bizId;
    private boolean reliability;
    private transient int pageSize;
    private transient int cachedDataSize;

    public Notify() {
        this.gmtExpire = 0L;
        this.reliability = true;
    }

    public Notify(String bizId, Call<?> call) {
        this(bizId, call, true);
    }

    public Notify(String bizId, Call<?> call, boolean reliability) {
        this.gmtExpire = 0L;
        this.reliability = true;
        this.setDomain(call);
        this.bizId = bizId;
        this.pageSize = -1;
        this.cachedDataSize = -1;
        this.reliability = reliability;
    }

    public long getGmtExpire() {
        return this.gmtExpire;
    }

    public void setGmtExpire(long gmtExpire) {
        this.gmtExpire = gmtExpire;
    }

    public boolean isReliability() {
        return this.reliability;
    }

    public void setReliability(boolean reliability) {
        this.reliability = reliability;
    }

    public String getBizId() {
        return this.bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCachedDataSize() {
        return this.cachedDataSize;
    }

    public void setCachedDataSize(int cachedDataSize) {
        this.cachedDataSize = cachedDataSize;
    }

    public String findNotifyName() {
        return ((Call)this.getDomain()).getServiceUri().getRawPathWithoutParameter();
    }

    public boolean hasExpired() {
        if (this.gmtExpire == 0L) {
            return false;
        } else {
            return System.currentTimeMillis() > this.gmtExpire;
        }
    }

    public boolean equals(Object obj) {
        try {
            return EqualsBuilder.reflectionEquals(this, obj);
        } catch (Exception var3) {
            return super.equals(obj);
        }
    }

    public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        } catch (Exception var2) {
            return super.toString();
        }
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.client.model;

import com.zbjdl.common.utils.event.lang.uri.WebURI;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class Call<T> implements Serializable {
    private static final long serialVersionUID = 1296032368171224792L;
    protected int code;
    protected WebURI serviceUri;
    protected boolean isRemote = false;
    protected T parameter;

    public Call() {
    }

    public WebURI getServiceUri() {
        return this.serviceUri;
    }

    public void setServiceUri(WebURI serviceUri) {
        this.serviceUri = serviceUri;
    }

    public boolean isRemote() {
        return this.isRemote;
    }

    public void setRemote(boolean isRemote) {
        this.isRemote = isRemote;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getParameter() {
        return this.parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    public String findCallName() {
        return this.getServiceUri().getRawPathWithoutParameter();
    }

    public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        } catch (Exception var3) {
            StringBuffer buff = new StringBuffer();
            buff.append("code=").append(this.code);
            buff.append("isRemote=").append("isRemote");
            buff.append("serviceUri=").append(this.serviceUri);
            return buff.toString();
        }
    }

    public boolean equals(Object obj) {
        try {
            return EqualsBuilder.reflectionEquals(this, obj);
        } catch (Exception var3) {
            return super.equals(obj);
        }
    }
}

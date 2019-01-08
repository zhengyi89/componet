//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.runtime;

import com.zbjdl.common.utils.event.model.BaseModel;

public class ProductRuntime extends BaseModel {
    private static final long serialVersionUID = -3135679560010581418L;
    private String url;
    private String oriUrl;
    private String processId;
    private String sessionId;
    private long productCode;
    private String productVersion;
    private String userId;
    private String userName;
    private int logonType = -1;
    private long time = System.currentTimeMillis();
    private String referer;
    private ProductEnvironment environment = new ProductEnvironment();
    private String partnerId;
    private boolean ignoreCache = false;

    public ProductRuntime() {
    }

    public ProductEnvironment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(ProductEnvironment environment) {
        this.environment = environment;
    }

    public long getProductCode() {
        return this.productCode;
    }

    public void setProductCode(long productCode) {
        this.productCode = productCode;
    }

    public String getProductVersion() {
        return this.productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public int getLogonType() {
        return this.logonType;
    }

    public void setLogonType(int logonType) {
        this.logonType = logonType;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProcessId() {
        return this.processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getReferer() {
        return this.referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean isIgnoreCache() {
        return this.ignoreCache;
    }

    public void setIgnoreCache(boolean ignoreCache) {
        this.ignoreCache = ignoreCache;
    }

    public String getOriUrl() {
        return this.oriUrl;
    }

    public void setOriUrl(String oriUrl) {
        this.oriUrl = oriUrl;
    }
}

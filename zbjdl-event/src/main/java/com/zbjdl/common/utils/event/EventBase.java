package com.zbjdl.common.utils.event;

import com.zbjdl.common.utils.event.model.BaseModel;

public class EventBase extends BaseModel {
    private static final long serialVersionUID = 1578713267857951997L;
    protected long gmtCreate = System.currentTimeMillis();
    protected long gmtModify;
    protected Object domain;

    public EventBase() {
        this.gmtModify = this.gmtCreate;
    }

    public long getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModify() {
        return this.gmtModify;
    }

    public void setGmtModify(long gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Object getDomain() {
        return this.domain;
    }

    public void setDomain(Object domain) {
        this.domain = domain;
    }
}

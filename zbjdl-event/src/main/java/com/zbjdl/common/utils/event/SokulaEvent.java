//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event;

import com.zbjdl.common.utils.ThreadContextUtils;
import com.zbjdl.common.utils.event.lang.invoker.SpringInvokerUtils;
import com.zbjdl.common.utils.event.runtime.ProductContextHolder;
import com.zbjdl.common.utils.event.runtime.ProductRuntime;
import com.zbjdl.common.utils.event.utils.DateUtils;
import com.zbjdl.common.utils.event.utils.IDUtils;
import org.apache.commons.lang.StringUtils;

public class SokulaEvent extends EventBase {
    private static final long serialVersionUID = 6183890343078630062L;
    private String eventName;
    private ProductRuntime runtime;
    private long delayHandleTime;
    private String eventId;
    private String appName;
    private String guid;
    private transient Object returnObject;
    private transient boolean hasReturn;
    private transient boolean inTransaction;

    public SokulaEvent() {
        this((String)null, IDUtils.msgId(), -1L);
    }

    public SokulaEvent(String eventName) {
        this(eventName, IDUtils.msgId(), -1L);
    }

    public SokulaEvent(String eventName, String eventId, long delay) {
        this(eventName, eventId, (String)null, (String)null, delay);
    }

    public SokulaEvent(String eventName, String eventId, String appName, String guid, long delay) {
        this.hasReturn = false;
        this.inTransaction = false;
        if (StringUtils.isBlank(eventId)) {
            throw new RuntimeException("SokulaEvent ! eventId is null . eventName = " + eventName);
        } else {
            this.eventName = eventName;
            this.eventId = eventId;
            if (null != ProductContextHolder.getProductContext()) {
                this.runtime = ProductContextHolder.getProductContext().getProductRuntime();
            }

            this.appName = appName;
            this.guid = guid;
            if (StringUtils.isBlank(appName)) {
                if (ThreadContextUtils.contextInitialized()) {
                    this.appName = ThreadContextUtils.getContext().getAppName();
                    this.guid = ThreadContextUtils.getContext().getThreadUID();
                } else {
                    this.appName = SpringInvokerUtils.getContextName();
                    this.guid = eventId;
                }
            }

            this.gmtCreate = System.currentTimeMillis();
            if (delay > 0L) {
                this.delayHandleTime = System.currentTimeMillis() + delay;
            } else {
                this.delayHandleTime = System.currentTimeMillis();
            }

        }
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public ProductRuntime getRuntime() {
        return this.runtime;
    }

    public void setRuntime(ProductRuntime runtime) {
        this.runtime = runtime;
    }

    public long getDelayHandleTime() {
        return this.delayHandleTime;
    }

    public void setDelayHandleTime(long delayHandleTime) {
        this.delayHandleTime = delayHandleTime;
    }

    public long getDelay() {
        return System.currentTimeMillis() - this.delayHandleTime;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int hashCode() {
        return StringUtils.isNotBlank(this.eventId) ? this.eventId.hashCode() : super.hashCode();
    }

    public Object getReturnObject() {
        return this.returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.hasReturn = true;
        this.returnObject = returnObject;
    }

    public boolean isHasReturn() {
        return this.hasReturn;
    }

    public boolean isInTransaction() {
        return this.inTransaction;
    }

    public void setInTransaction(boolean inTransaction) {
        this.inTransaction = inTransaction;
    }

    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String toString() {
        String str = super.toString();
        str = str + " delayHandleTime=" + DateUtils.formatByLong(this.delayHandleTime, DateUtils.newFormat);
        return str;
    }
}

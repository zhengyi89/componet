//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.notify;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.core.fstore.QuickLocation;
import com.zbjdl.common.utils.event.lang.hessian.ext.AtomSerialization;
import java.io.IOException;
import org.apache.commons.lang.builder.EqualsBuilder;

public class AsyncReliableSokulaEvent extends SokulaEvent implements AtomSerialization {
    private static final long serialVersionUID = 7980396110037662644L;
    private transient QuickLocation location;
    private transient AsyncStatusEnum status;
    private transient int eventType;
    private transient Throwable exception;

    public AsyncReliableSokulaEvent() {
    }

    public AsyncReliableSokulaEvent(int eventType) {
        this.eventType = eventType;
    }

    public AsyncReliableSokulaEvent(SokulaEvent event, QuickLocation location, int eventType) {
        super(event.getEventName(), event.getEventId(), event.getAppName(), event.getGuid(), -1L);
        if (location.getGmtModify() < System.currentTimeMillis() - 120000L) {
            this.setDelayHandleTime(System.currentTimeMillis());
        } else {
            this.setDelayHandleTime(location.getGmtModify());
        }

        this.domain = event.getDomain();
        this.gmtCreate = event.getGmtCreate();
        this.gmtModify = System.currentTimeMillis();
        this.location = location;
        this.status = AsyncStatusEnum.FILE_QUEUE;
        this.eventType = eventType;
    }

    public QuickLocation getLocation() {
        return this.location;
    }

    public boolean equals(Object obj) {
        try {
            return EqualsBuilder.reflectionEquals(this, obj);
        } catch (Exception var3) {
            return super.equals(obj);
        }
    }

    public AsyncStatusEnum getStatus() {
        return this.status;
    }

    public void setStatus(AsyncStatusEnum status) {
        this.status = status;
    }

    public int getEventType() {
        return this.eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Throwable getException() {
        return this.exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public void readObjectData(AbstractHessianInput buffer) throws IOException {
        this.gmtCreate = buffer.readLong();
        this.gmtModify = buffer.readLong();
        this.domain = buffer.readObject();
    }

    public void writeObjectData(AbstractHessianOutput buffer) throws IOException {
        buffer.writeLong(this.gmtCreate);
        buffer.writeLong(this.gmtModify);
        buffer.writeObject(this.domain);
    }

    public boolean canHandle() {
        return this.getDelayHandleTime() <= System.currentTimeMillis();
    }

    public boolean isInHandleWindow() {
        return this.getDelayHandleTime() < NotifyDelayStrategy.getNextTimeWindow();
    }

    public boolean isOverHandleWindow() {
        return this.getDelayHandleTime() < NotifyDelayStrategy.getNextTimeWindow() - 120000L;
    }

    public boolean canRecur() {
        long nextTime = NotifyDelayStrategy.getNextTimeWindow();
        boolean canRecur = this.getDelayHandleTime() > nextTime - 120000L && this.getDelayHandleTime() < nextTime;
        return canRecur;
    }
}

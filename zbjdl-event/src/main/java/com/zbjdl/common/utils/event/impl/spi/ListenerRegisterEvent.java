//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.zbjdl.common.utils.event.EventListener;
import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.lang.hessian.ext.AtomSerialization;
import java.io.IOException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ListenerRegisterEvent extends SokulaEvent implements AtomSerialization {
    private static final long serialVersionUID = -1002431048772972443L;
    public static final String EVENT_NAME = "PUB_SUB_LISTENER_REGISTER";
    private EventListener<SokulaEvent> listener;

    public ListenerRegisterEvent() {
    }

    public ListenerRegisterEvent(String listenerdEventName, EventListener<SokulaEvent> listener) {
        super("PUB_SUB_LISTENER_REGISTER");
        this.domain = listenerdEventName;
        this.listener = listener;
    }

    public EventListener<SokulaEvent> getListener() {
        return this.listener;
    }

    public String getListenerdEventName() {
        return (String)this.domain;
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
}

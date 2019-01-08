//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.chain;

import com.zbjdl.common.utils.event.SokulaEvent;

public class EventContext {
    public SokulaEvent event;
    public boolean supportMultiSub;
    public Object result;

    public EventContext(SokulaEvent event, boolean supportMultiSub) {
        this.event = event;
        this.supportMultiSub = supportMultiSub;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return this.result;
    }

    public SokulaEvent getEvent() {
        return this.event;
    }

    public boolean isSupportMultiSub() {
        return this.supportMultiSub;
    }

    public void setEvent(SokulaEvent event) {
        this.event = event;
    }
}

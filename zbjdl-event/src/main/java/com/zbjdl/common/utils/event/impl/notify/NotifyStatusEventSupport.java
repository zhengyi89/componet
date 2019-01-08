//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.notify;

public class NotifyStatusEventSupport {
    private NotifyStatusEventListener listener;

    public NotifyStatusEventSupport() {
    }

    public void fireEvent(AsyncReliableSokulaEvent event) {
        this.listener.handleEvent(event);
    }

    public void setListener(NotifyStatusEventListener listener) {
        this.listener = listener;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.ext;

public abstract class BaseEventListener {
    public BaseEventListener() {
        BaseEventDispatcher.registerListener(this.getListenedEventName(), this);
    }

    public abstract String getListenedEventName();

    public abstract void doAction(Object... var1);
}


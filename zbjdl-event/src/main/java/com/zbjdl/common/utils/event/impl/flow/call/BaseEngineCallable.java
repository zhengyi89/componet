//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.flow.call;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.impl.EngineCallable;

public abstract class BaseEngineCallable implements EngineCallable {
    public BaseEngineCallable() {
    }

    public boolean accept(SokulaEvent event) {
        return true;
    }

    public Object handle(SokulaEvent event, boolean supportMultiSub) {
        return this.doHandle(event, supportMultiSub);
    }

    protected abstract Object doHandle(SokulaEvent var1, boolean var2);
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.call;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.impl.CallerChainAdaptor;
import com.zbjdl.common.utils.event.impl.InvokeModel;

public abstract class AbstractBrokerImpl implements InvokeModel {
    private CallerChainAdaptor callerChainAdaptor;

    public AbstractBrokerImpl() {
    }

    public void setCallerChainAdaptor(CallerChainAdaptor callerChainAdaptor) {
        this.callerChainAdaptor = callerChainAdaptor;
    }

    protected void eventCall(SokulaEvent event) {
        this.callerChainAdaptor.eventCall(event, false);
    }
}

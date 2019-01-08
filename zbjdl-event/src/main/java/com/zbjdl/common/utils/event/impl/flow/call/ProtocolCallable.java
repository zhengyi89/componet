//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.flow.call;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.impl.spi.ProtocolInvoker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProtocolCallable extends BaseEngineCallable {
    private List<ProtocolInvoker> invokers = new ArrayList();

    public ProtocolCallable() {
    }

    public void init() {
    }

    public void destroy() {
        this.invokers.clear();
    }

    public void setInvokers(List<ProtocolInvoker> invokers) {
        this.invokers = invokers;
    }

    public boolean accept(SokulaEvent event) {
        Iterator var2 = this.invokers.iterator();

        ProtocolInvoker invoker;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            invoker = (ProtocolInvoker)var2.next();
        } while(!invoker.supportProtocol(event.getEventName()));

        return true;
    }

    public Object doHandle(SokulaEvent event, boolean supportMultiSub) {
        ProtocolInvoker invoker = null;
        Iterator var4 = this.invokers.iterator();

        while(var4.hasNext()) {
            ProtocolInvoker invokerTemp = (ProtocolInvoker)var4.next();
            if (invokerTemp.supportProtocol(event.getEventName())) {
                invoker = invokerTemp;
                break;
            }
        }

        return this.callListener(invoker, event);
    }

    private Object callListener(ProtocolInvoker invoker, SokulaEvent event) {
        return invoker.handle(event);
    }
}

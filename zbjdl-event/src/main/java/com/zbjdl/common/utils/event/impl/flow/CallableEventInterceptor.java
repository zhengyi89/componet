//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.flow;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.core.chain.EventContext;
import com.zbjdl.common.utils.event.core.chain.EventInterceptorChain;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.impl.EngineCallable;
import com.zbjdl.common.utils.event.impl.EventInterceptor;
import com.zbjdl.common.utils.event.lang.diagnostic.Profiler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallableEventInterceptor implements EventInterceptor {
    private List<EngineCallable> engineListeners = new ArrayList();

    public CallableEventInterceptor() {
    }

    private Object eventCall(SokulaEvent event, boolean supportMultiSub) {
        Profiler.enter("CallableEventInterceptor.eventCall");

        Object var5;
        try {
            Iterator var3 = this.engineListeners.iterator();

            EngineCallable callable;
            do {
                if (!var3.hasNext()) {
                    throw new EventRuntimeException(97100004, "Callable listener is not exist. URI：" + event.getEventName());
                }

                callable = (EngineCallable)var3.next();
            } while(!callable.accept(event));

            var5 = callable.handle(event, supportMultiSub);
        } finally {
            Profiler.release();
        }

        return var5;
    }

    public void doFilter(EventContext eventContext, EventInterceptorChain chain) {
        Object result = this.eventCall(eventContext.getEvent(), eventContext.isSupportMultiSub());
        eventContext.getEvent().setReturnObject(result);
        chain.doFilter(eventContext);
    }

    public void setEngineListeners(List<EngineCallable> engineListeners) {
        this.engineListeners = engineListeners;
    }
}

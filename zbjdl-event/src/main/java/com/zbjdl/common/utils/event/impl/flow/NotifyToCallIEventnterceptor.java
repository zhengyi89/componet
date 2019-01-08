//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.flow;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.client.model.Call;
import com.zbjdl.common.utils.event.client.model.Notify;
import com.zbjdl.common.utils.event.core.chain.EventContext;
import com.zbjdl.common.utils.event.core.chain.EventInterceptorChain;
import com.zbjdl.common.utils.event.impl.EventInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyToCallIEventnterceptor implements EventInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(NotifyToCallIEventnterceptor.class);

    public NotifyToCallIEventnterceptor() {
    }

    public void doFilter(EventContext eventContext, EventInterceptorChain chain) {
        SokulaEvent oldEvent = eventContext.getEvent();
        if (oldEvent.getDomain() instanceof Notify) {
            Notify notify = (Notify)oldEvent.getDomain();
            Call<?> call = (Call)notify.getDomain();
            SokulaEvent callEvent = new SokulaEvent(oldEvent.getEventName(), oldEvent.getEventId(), oldEvent.getAppName(), oldEvent.getGuid(), oldEvent.getDelay());
            callEvent.setDomain(call);
            eventContext.setEvent(callEvent);
            chain.doFilter(eventContext);
            oldEvent.setReturnObject(callEvent.getReturnObject());
            eventContext.setEvent(oldEvent);
            Object result = eventContext.getResult();
            if (null != result && result instanceof Exception) {
                logger.error("Notify : " + notify, (Exception)result);
                if (!notify.isReliability()) {
                    eventContext.setResult((Object)null);
                }
            }
        } else {
            chain.doFilter(eventContext);
        }

    }
}

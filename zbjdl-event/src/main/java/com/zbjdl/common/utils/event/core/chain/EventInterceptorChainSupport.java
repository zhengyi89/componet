//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.chain;

import com.zbjdl.common.utils.event.impl.EventInterceptor;
import com.zbjdl.common.utils.event.lang.diagnostic.Profiler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventInterceptorChainSupport implements EventInterceptorChain {
    private static final Logger logger = LoggerFactory.getLogger(EventInterceptorChainSupport.class);
    private List<EventInterceptor> interceptors = new ArrayList();

    public EventInterceptorChainSupport(List<EventInterceptor> chains) {
        Iterator var2 = chains.iterator();

        while(var2.hasNext()) {
            EventInterceptor wi = (EventInterceptor)var2.next();
            this.interceptors.add(wi);
        }

    }

    public void doFilter(EventContext eventContext) {
        if (this.interceptors.size() > 0) {
            EventInterceptor interceptor = (EventInterceptor)this.interceptors.remove(0);
            if (logger.isDebugEnabled()) {
                logger.debug("EventInterceptor : " + interceptor.getClass().getName() + " -- begin do.");
            }

            Profiler.enter(interceptor.getClass().getSimpleName());
            interceptor.doFilter(eventContext, this);
            Profiler.release();
            if (logger.isDebugEnabled()) {
                logger.debug("EventInterceptor : " + interceptor.getClass().getName() + " -- do success.");
            }
        }

    }
}

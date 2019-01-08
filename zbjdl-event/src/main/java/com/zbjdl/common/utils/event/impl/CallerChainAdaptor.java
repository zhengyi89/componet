//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.core.chain.EventContext;
import com.zbjdl.common.utils.event.core.chain.EventInterceptorChain;
import com.zbjdl.common.utils.event.runtime.ProductContext;
import com.zbjdl.common.utils.event.runtime.ProductContextHolder;
import com.zbjdl.common.utils.event.runtime.ProductRuntime;

public class CallerChainAdaptor {
    private EventInterceptorChain eventInterceptorChain;

    public CallerChainAdaptor() {
    }

    public Object eventCall(SokulaEvent event, boolean supportMultiSub) {
        ProductContext context = ProductContextHolder.begin();
        ProductRuntime oldRuntime = context.getProductRuntime();

        Object var6;
        try {
            context.setProductRuntime(event.getRuntime());
            EventContext eventContext = new EventContext(event, supportMultiSub);
            this.eventInterceptorChain.doFilter(eventContext);
            var6 = eventContext.getResult();
        } finally {
            context.setProductRuntime(oldRuntime);
            ProductContextHolder.end();
        }

        return var6;
    }

    public void setEventInterceptorChain(EventInterceptorChain eventInterceptorChain) {
        this.eventInterceptorChain = eventInterceptorChain;
    }
}

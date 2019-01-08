//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.chain;

import com.zbjdl.common.utils.event.impl.EventInterceptor;
import java.util.ArrayList;
import java.util.List;

public class EventInterceptorChainImpl implements EventInterceptorChain {
    private List<EventInterceptor> chains = new ArrayList();

    public EventInterceptorChainImpl() {
    }

    public void doFilter(EventContext eventContext) {
        if (null != this.chains || this.chains.size() > 0) {
            EventInterceptorChainSupport support = new EventInterceptorChainSupport(this.chains);
            support.doFilter(eventContext);
        }

    }

    public void setChains(List<EventInterceptor> chains) {
        this.chains = chains;
    }
}

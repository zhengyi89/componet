//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.flow;

import com.zbjdl.common.utils.event.core.chain.EventContext;
import com.zbjdl.common.utils.event.core.chain.EventInterceptorChain;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.impl.EventInterceptor;
import com.zbjdl.common.utils.event.impl.notify.AsyncReliableSokulaEvent;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadEventCheckerEventInterceptor implements EventInterceptor {
    private static long ALIVE_WINDOW = 86400000L;
    private static int RETRY_COUNT = 10;
    private static final Logger logger = LoggerFactory.getLogger(DeadEventCheckerEventInterceptor.class);

    public DeadEventCheckerEventInterceptor() {
        int hour = SystemConfigUtils.getIntProperty("event.commit.alive.hour", 24);
        if (hour < 1) {
            hour = 1;
        }

        ALIVE_WINDOW = (long)(3600000 * hour);
        RETRY_COUNT = SystemConfigUtils.getIntProperty("event.retry.count", 10);
    }

    public void doFilter(EventContext eventContext, EventInterceptorChain chain) {
        if (eventContext.getEvent() instanceof AsyncReliableSokulaEvent) {
            AsyncReliableSokulaEvent arevent = (AsyncReliableSokulaEvent)eventContext.getEvent();
            long gmtCreate = arevent.getGmtCreate();
            if (System.currentTimeMillis() - gmtCreate > ALIVE_WINDOW || arevent.getLocation().getErrCount() > RETRY_COUNT) {
                logger.info("EVENT IS DEAD !!!! event = " + arevent);
                throw new EventRuntimeException(99201007);
            }
        }

        chain.doFilter(eventContext);
    }
}

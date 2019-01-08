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

public class NewEventCheckerEventInterceptor implements EventInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(NewEventCheckerEventInterceptor.class);
    private static long ALIVE_WINDOW = 1800000L;

    public NewEventCheckerEventInterceptor() {
        int minute = SystemConfigUtils.getIntProperty("event.new.alive.minute", 30);
        if (minute < 1) {
            minute = 30;
        }

        ALIVE_WINDOW = (long)('\uea60' * minute);
    }

    public void doFilter(EventContext eventContext, EventInterceptorChain chain) {
        if (eventContext.getEvent() instanceof AsyncReliableSokulaEvent) {
            AsyncReliableSokulaEvent arevent = (AsyncReliableSokulaEvent)eventContext.getEvent();
            if (arevent.getLocation().getStatus() == 0) {
                long gmtCreate = arevent.getGmtCreate();
                if (System.currentTimeMillis() - gmtCreate > ALIVE_WINDOW) {
                    logger.info("NEW EVENT IS DEAD !!!! event = " + arevent);
                    throw new EventRuntimeException(99201007);
                }

                if (logger.isDebugEnabled()) {
                    logger.info("EVENT IS NEW !!!! event = " + arevent);
                }

                throw new EventRuntimeException(99201008);
            }
        }

        chain.doFilter(eventContext);
    }
}

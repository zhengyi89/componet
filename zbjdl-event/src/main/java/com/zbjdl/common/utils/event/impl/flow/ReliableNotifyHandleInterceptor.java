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
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReliableNotifyHandleInterceptor implements EventInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ReliableNotifyHandleInterceptor.class);

    public ReliableNotifyHandleInterceptor() {
    }

    public void doFilter(EventContext eventContext, EventInterceptorChain chain) {
        if (eventContext.getEvent() instanceof AsyncReliableSokulaEvent) {
            AsyncReliableSokulaEvent arevent = (AsyncReliableSokulaEvent)eventContext.getEvent();
            if (arevent.isOverHandleWindow()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("EVENT HAS OVER TIME WINDOW !!!! event = " + arevent);
                }

                throw new EventRuntimeException(99201005);
            }

            chain.doFilter(eventContext);
            Object result = eventContext.getResult();
            if (logger.isDebugEnabled()) {
                logger.debug("RESULT !!! " + result);
            }

            this.handleException(result);
            Object bResult = eventContext.getEvent().getReturnObject();
            if (logger.isDebugEnabled()) {
                logger.debug("biz result !!! " + result);
            }

            this.handleException(bResult);
        } else {
            chain.doFilter(eventContext);
        }

    }

    private void handleException(Object result) {
        if (null != result) {
            if (result instanceof RuntimeException) {
                if (logger.isDebugEnabled()) {
                    logger.error("", (RuntimeException)result);
                }

                throw (RuntimeException)result;
            }

            if (result instanceof Throwable) {
                if (logger.isDebugEnabled()) {
                    logger.error("", (Throwable)result);
                }

                Throwable root = ExceptionUtils.getRootCause((Throwable)result);
                if (root != null && root instanceof RuntimeException) {
                    throw (RuntimeException)root;
                }

                throw new RuntimeException((Throwable)result);
            }
        }

    }
}

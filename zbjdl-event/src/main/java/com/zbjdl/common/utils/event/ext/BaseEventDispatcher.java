//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.ext;

import com.zbjdl.common.utils.ThreadContextType;
import com.zbjdl.common.utils.ThreadContextUtils;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.impl.EventEngineImpl;
import com.zbjdl.common.utils.event.lang.diagnostic.Profiler;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseEventDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(BaseEventDispatcher.class);
    private static Map<String, BaseEventListener> listeners = new HashMap();

    public BaseEventDispatcher() {
    }

    public void handle(String eventName, Object... params) {
        if (logger.isInfoEnabled()) {
            logger.info("handleEvent : " + eventName);
        }

        if (listeners.containsKey(eventName)) {
            Profiler.start("BaseEventDispatcher.run");
            boolean var12 = false;

            try {
                var12 = true;
                ((BaseEventListener)listeners.get(eventName)).doAction(params);
                var12 = false;
            } catch (Throwable var13) {
                logger.error("", var13);
                if (var13 instanceof RuntimeException) {
                    throw (RuntimeException)var13;
                }

                Throwable root = ExceptionUtils.getRootCause(var13);
                if (root instanceof RuntimeException) {
                    throw (RuntimeException)root;
                }

                throw new EventRuntimeException(99001001, var13.getMessage(), var13);
            } finally {
                if (var12) {
                    Profiler.release();
                    long elapseTime = Profiler.getDuration();
                    if (logger.isDebugEnabled() || elapseTime > (long)EventEngineImpl.THRESHOLD) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("URL:").append(eventName);
                        builder.append(" used P = ").append(elapseTime).append("ms.\r\n");
                        builder.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                        logger.info(builder.toString());
                    }

                    Profiler.reset();
                }
            }

            Profiler.release();
            long elapseTime = Profiler.getDuration();
            if (logger.isDebugEnabled() || elapseTime > (long)EventEngineImpl.THRESHOLD) {
                StringBuilder builder = new StringBuilder();
                builder.append("URL:").append(eventName);
                builder.append(" used P = ").append(elapseTime).append("ms.\r\n");
                builder.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                logger.info(builder.toString());
            }

            Profiler.reset();
            if (logger.isDebugEnabled()) {
                logger.debug("[3] 异步事件派发完成  eventName : " + eventName + " | listener : " + listeners.get(eventName));
            }

        } else {
            logger.error("[2.x] listener for this eventName : " + eventName + " is no exist!");
            throw new NullPointerException("listener doesn't find . eventName : " + eventName);
        }
    }

    /** @deprecated */
    @Deprecated
    public void handle(String eventName, String appName, String guid, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug("[1] BaseEventDispatcher.handle, eventName=" + eventName + " | eventName=" + eventName + " | listener =" + listeners.get(eventName));
        }

        if (listeners.containsKey(eventName)) {
            Profiler.start("BaseEventDispatcher.run");
            boolean var14 = false;

            try {
                var14 = true;
                if (ThreadContextUtils.contextInitialized()) {
                    ThreadContextUtils.clearContext();
                }

                ThreadContextUtils.initContext(appName, guid, ThreadContextType.TASK);
                ((BaseEventListener)listeners.get(eventName)).doAction(params);
                var14 = false;
            } catch (Throwable var15) {
                logger.error("", var15);
                if (var15 instanceof RuntimeException) {
                    throw (RuntimeException)var15;
                }

                Throwable root = ExceptionUtils.getRootCause(var15);
                if (root instanceof RuntimeException) {
                    throw (RuntimeException)root;
                }

                throw new EventRuntimeException(99001001, var15.getMessage(), var15);
            } finally {
                if (var14) {
                    Profiler.release();
                    long elapseTime = Profiler.getDuration();
                    if (logger.isDebugEnabled() || elapseTime > (long)EventEngineImpl.THRESHOLD) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("URL:").append(eventName);
                        builder.append(" used P = ").append(elapseTime).append("ms.\r\n");
                        builder.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                        logger.info(builder.toString());
                    }

                    Profiler.reset();
                    ThreadContextUtils.clearContext();
                }
            }

            Profiler.release();
            long elapseTime = Profiler.getDuration();
            if (logger.isDebugEnabled() || elapseTime > (long)EventEngineImpl.THRESHOLD) {
                StringBuilder builder = new StringBuilder();
                builder.append("URL:").append(eventName);
                builder.append(" used P = ").append(elapseTime).append("ms.\r\n");
                builder.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                logger.info(builder.toString());
            }

            Profiler.reset();
            ThreadContextUtils.clearContext();
            if (logger.isDebugEnabled()) {
                logger.debug("[3] 异步事件派发完成  eventName : " + eventName + " | listener : " + listeners.get(eventName));
            }

        } else {
            logger.error("[2.x] listener for this eventName : " + eventName + " is no exist!");
            throw new NullPointerException("listener doesn't find . eventName : " + eventName);
        }
    }

    public static void registerListener(String name, BaseEventListener listener) {
        listeners.put(name, listener);
    }
}

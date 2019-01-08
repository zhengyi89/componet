//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventEngineImpl {
    private static final Logger logger = LoggerFactory.getLogger(EventEngineImpl.class);
    public static int THRESHOLD = 200;
    public static String PERF_PREFIX = "\tPerfLogger ";
    private static EventEngineImpl INSTANCE;
    private List<InvokeModel> providers;

    public EventEngineImpl() {
        THRESHOLD = SystemConfigUtils.getIntProperty("event.perf.threshold", THRESHOLD);
        INSTANCE = this;
    }

    public void setProviders(List<InvokeModel> providers) {
        this.providers = providers;
    }

    public Object dispatchHelp(SokulaEvent event, InvokeModelEnum type, CountDownLatch doneSignal) {
        logger.info("dispatchEvent event = " + event);
        Iterator var4 = this.providers.iterator();

        InvokeModel provider;
        do {
            if (!var4.hasNext()) {
                return null;
            }

            provider = (InvokeModel)var4.next();
        } while(!provider.accept(type));

        return provider.broker(event, doneSignal);
    }

    public static Object dispatch(SokulaEvent event, InvokeModelEnum type, CountDownLatch doneSignal) {
        if (isInited()) {
            return INSTANCE.dispatchHelp(event, type, doneSignal);
        } else {
            throw new EventRuntimeException(99001001, "yeepay event is not inited!");
        }
    }

    public static boolean isInited() {
        return INSTANCE != null;
    }
}

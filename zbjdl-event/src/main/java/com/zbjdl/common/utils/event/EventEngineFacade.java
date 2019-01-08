package com.zbjdl.common.utils.event;

import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.impl.EventEngineImpl;
import com.zbjdl.common.utils.event.impl.InvokeModelEnum;
import com.zbjdl.common.utils.event.impl.spi.ListenerRegisterEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EventEngineFacade {
    public static final int FS_MANAGER_IS_NOT_INITED = 97100001;
    public static final int QUEUE_NAME_IS_NULL = 97100002;
    public static final int ASYNC_GET_RESULT_EXCEPTION = 97100003;
    public static final int NO_LISTENER_EXCEPTION = 97100004;

    public EventEngineFacade() {
    }

    public static Object call(SokulaEvent event) {
        return EventEngineImpl.dispatch(event, InvokeModelEnum.CALL, (CountDownLatch)null);
    }

    public static void send(SokulaEvent event, boolean inTransaction) {
        send((String)null, -1, -1, (String)null, event, inTransaction);
    }

    private static void send(String queueName, int pageSize, int cachedDataSize, String bizId, SokulaEvent event, boolean inTransaction) {
        event.setInTransaction(inTransaction);
        EventEngineImpl.dispatch(event, InvokeModelEnum.NOTIFY, (CountDownLatch)null);
    }

    public static Object request(SokulaEvent event, long timeout) {
        Object[] result = request(new SokulaEvent[]{event}, timeout);
        return null != result && result.length == 1 ? result[0] : null;
    }

    public static Object[] request(SokulaEvent[] events, long timeout) {
        int size = events.length;
        CountDownLatch doneSignal = new CountDownLatch(size);
        SokulaEvent[] var5 = events;
        int var6 = events.length;

        int i;
        for(i = 0; i < var6; ++i) {
            SokulaEvent event = var5[i];
            event.setDelayHandleTime(System.currentTimeMillis() + timeout);
            EventEngineImpl.dispatch(event, InvokeModelEnum.REQUEST, doneSignal);
        }

        if (timeout < 1L) {
            timeout = 250L;
        }

        try {
            doneSignal.await(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception var9) {
            throw new EventRuntimeException(97100003, var9);
        }

        boolean requestComplete = true;
        Object[] results = new Object[size];

        for(i = 0; i < size; ++i) {
            if (events[i].isHasReturn()) {
                results[i] = events[i].getReturnObject();
            } else {
                results[i] = null;
                requestComplete = false;
            }
        }

        if (!requestComplete) {
            throw new EventRuntimeException(97100003, "请求数据不完整.");
        } else {
            return results;
        }
    }

    public static void addEventListener(String eventName, EventListener<SokulaEvent> listener) {
        call(new ListenerRegisterEvent(eventName, listener));
    }
}

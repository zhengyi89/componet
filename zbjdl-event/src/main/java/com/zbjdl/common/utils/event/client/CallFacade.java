package com.zbjdl.common.utils.event.client;

import com.zbjdl.common.utils.event.EventEngineFacade;
import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.client.model.Call;
import com.zbjdl.common.utils.event.client.model.Notify;
import com.zbjdl.common.utils.event.utils.IDUtils;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang.StringUtils;

public class CallFacade {
    private static long DAY = 86400000L;
    private static AtomicLong counter;
    private static long MAX;

    public CallFacade() {
    }

    public static final Object request(Call<?> call, long timeout) {
        SokulaEvent event = new SokulaEvent(call.findCallName());
        event.setDomain(call);
        return EventEngineFacade.request(event, timeout);
    }

    public static final Object[] request(Call<?>[] calls, long timeout) {
        SokulaEvent[] events = new SokulaEvent[calls.length];

        for(int i = 0; i < calls.length; ++i) {
            SokulaEvent event = new SokulaEvent(calls[i].findCallName());
            event.setDomain(calls[i]);
            events[i] = event;
        }

        return EventEngineFacade.request(events, timeout);
    }

    public static final Object call(Call<?> call) {
        SokulaEvent event = new SokulaEvent(call.findCallName());
        event.setDomain(call);
        return EventEngineFacade.call(event);
    }

    public static final Object call211(Call<?> call) throws Exception {
        Object obj = call(call);
        if (obj instanceof Exception) {
            throw (Exception)obj;
        } else {
            return obj;
        }
    }

    public static final void notify(Notify notify) throws Exception {
        notify(notify, 0L, false);
    }

    public static final void notify(Notify notify, long delay, boolean inTransaction) throws Exception {
        if (null != notify.getDomain()) {
            StringBuilder ids = new StringBuilder();
            if (StringUtils.isBlank(notify.getBizId())) {
                ids.append(IDUtils.msgId());
            } else {
                ids.append(notify.getBizId());
            }

            long sequence = counter.incrementAndGet();
            sequence %= MAX;
            ids.append("_");
            ids.append(sequence);
            SokulaEvent event = new SokulaEvent(notify.findNotifyName(), ids.toString(), delay);
            event.setDomain(notify);
            EventEngineFacade.send(event, inTransaction);
        } else {
            throw new IllegalArgumentException("domain is null.");
        }
    }

    static {
        counter = new AtomicLong(System.currentTimeMillis() % DAY);
        MAX = 9379L;
    }
}

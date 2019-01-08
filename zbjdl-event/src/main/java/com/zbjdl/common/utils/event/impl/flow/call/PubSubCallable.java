//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.flow.call;

import com.zbjdl.common.utils.event.EventListener;
import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.impl.spi.ListenerRegisterEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubSubCallable extends BaseEngineCallable {
    private static final Logger logger = LoggerFactory.getLogger(PubSubCallable.class);
    private Map<String, List<EventListener<SokulaEvent>>> listenerMap = new ConcurrentHashMap();

    public PubSubCallable() {
    }

    public void init() {
        this.addEventListener("PUB_SUB_LISTENER_REGISTER", new PubSubCallable.ListerRegister(this));
    }

    public void destroy() {
        this.listenerMap.clear();
    }

    private void addEventListener(String eventName, EventListener<SokulaEvent> listener) {
        if (logger.isDebugEnabled()) {
            logger.debug("addEventListener : eventName = " + eventName + " | listener = " + listener);
        }

        List<EventListener<SokulaEvent>> listeners = (List)this.listenerMap.get(eventName);
        if (null == listeners) {
            listeners = new ArrayList();
            this.listenerMap.put(eventName, listeners);
        }

        if (!((List)listeners).contains(listener)) {
            ((List)listeners).add(listener);
        }

    }

    public boolean accept(SokulaEvent event) {
        return this.listenerMap.containsKey(event.getEventName());
    }

    public Object doHandle(SokulaEvent event, boolean supportMultiSub) {
        List<EventListener<SokulaEvent>> listeners = (List)this.listenerMap.get(event.getEventName());
        if (supportMultiSub) {
            if (listeners.size() > 1) {
                List<Object> result = new ArrayList();
                Object obj = null;
                Iterator var6 = listeners.iterator();

                while(var6.hasNext()) {
                    EventListener<SokulaEvent> listener = (EventListener)var6.next();
                    obj = this.callListener(listener, event);
                    if (null != obj) {
                        result.add(obj);
                    }
                }

                return result;
            }
        } else if (listeners.size() > 1) {
            logger.warn("Queue has multi listener when don't call/request model . QueueName : " + event.getEventName() + " | Listeners : " + ArrayUtils.toString(listeners));
        }

        return this.callListener((EventListener)listeners.get(0), event);
    }

    private Object callListener(EventListener<SokulaEvent> listener, SokulaEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("pub/sub call: listener = " + listener + " | event = " + event);
        }

        return listener.handle(event);
    }

    private static class ListerRegister implements EventListener<SokulaEvent> {
        private PubSubCallable parent;

        private ListerRegister(PubSubCallable parent) {
            this.parent = parent;
        }

        public Object handle(SokulaEvent event) {
            if (event instanceof ListenerRegisterEvent) {
                ListenerRegisterEvent e2 = (ListenerRegisterEvent)event;
                this.parent.addEventListener(e2.getListenerdEventName(), e2.getListener());
            }

            return null;
        }
    }
}

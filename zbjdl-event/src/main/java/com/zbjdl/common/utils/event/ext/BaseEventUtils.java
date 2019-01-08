//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.ext;

import com.zbjdl.common.utils.ThreadContextUtils;
import com.zbjdl.common.utils.event.client.CallFacade;
import com.zbjdl.common.utils.event.client.model.Notify;
import com.zbjdl.common.utils.event.client.model.RpcCall;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.lang.uri.WebURI;
import com.zbjdl.common.utils.event.utils.IDUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseEventUtils {
    private static final Logger logger = LoggerFactory.getLogger(BaseEventUtils.class);
    private static WebURI BASE_EVENT_URI = new WebURI("spring:/baseEventDispatcher.hessian?actionName=handle");

    public BaseEventUtils() {
    }

    public static void sendEvent(String eventName, Object... params) {
        sendEvent(eventName, true, params);
    }

    public static void sendEventNotInTransaction(String eventName, Object... params) {
        sendEvent(eventName, false, params);
    }

    private static void sendEvent(String eventName, boolean inTransaction, Object... params) {
        try {
            String bizId = null;
            if (ThreadContextUtils.contextInitialized()) {
                bizId = ThreadContextUtils.getContext().getThreadUID();
            }

            if (StringUtils.isBlank(bizId)) {
                bizId = IDUtils.msgId();
            }

            RpcCall rpcCall = new RpcCall(BASE_EVENT_URI, new Object[]{eventName, params});
            Notify notify = new Notify(bizId, rpcCall, inTransaction);
            CallFacade.notify(notify, 0L, inTransaction);
        } catch (Exception var6) {
            logger.error("", var6);
            throw new EventRuntimeException(99001001, var6);
        }
    }
}

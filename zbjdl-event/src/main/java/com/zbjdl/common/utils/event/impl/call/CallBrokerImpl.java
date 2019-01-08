//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.call;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.impl.InvokeModel;
import com.zbjdl.common.utils.event.impl.InvokeModelEnum;
import java.util.concurrent.CountDownLatch;

public class CallBrokerImpl extends AbstractBrokerImpl implements InvokeModel {
    public CallBrokerImpl() {
    }

    public boolean accept(InvokeModelEnum type) {
        return type == InvokeModelEnum.CALL;
    }

    public Object broker(SokulaEvent event, CountDownLatch doneSignal) {
        this.eventCall(event);
        return event.getReturnObject();
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.notify;

import com.zbjdl.common.utils.event.core.thread.NotifyWorkerCallBack;

public interface NotifyExecutorService {
    boolean execute(NotifyWorkerCallBack var1);

    boolean isWaitIdle();

    boolean isThreadIdle();

    long getThreadIdleSize();

    NotifyQueue getNotifyQueue();

    String reportExecutorInfo();
}

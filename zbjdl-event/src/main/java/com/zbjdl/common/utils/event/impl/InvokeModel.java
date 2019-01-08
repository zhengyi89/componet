//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl;

import com.zbjdl.common.utils.event.SokulaEvent;
import java.util.concurrent.CountDownLatch;

public interface InvokeModel {
    boolean accept(InvokeModelEnum var1);

    Object broker(SokulaEvent var1, CountDownLatch var2);
}

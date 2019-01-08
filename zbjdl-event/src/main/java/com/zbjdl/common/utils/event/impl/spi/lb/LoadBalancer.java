//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.lb;

import com.zbjdl.common.utils.event.SokulaEvent;

public interface LoadBalancer {
    Processor getProcessor(SokulaEvent var1);

    void addProcessor(Processor var1);

    void removeProcessor(Processor var1);
}

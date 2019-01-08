//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi;

import com.zbjdl.common.utils.event.EventListener;
import com.zbjdl.common.utils.event.SokulaEvent;

public interface ProtocolInvoker extends EventListener<SokulaEvent> {
    boolean supportProtocol(String var1);
}

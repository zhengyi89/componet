//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl;

import com.zbjdl.common.utils.event.SokulaEvent;

public interface EngineCallable {
    boolean accept(SokulaEvent var1);

    Object handle(SokulaEvent var1, boolean var2);
}

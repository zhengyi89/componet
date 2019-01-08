//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.local;

import com.zbjdl.common.utils.cache.local.impl.EhcacheFactory;
import com.zbjdl.common.utils.cache.local.param.LocalParam;

public interface LocalCacheService {
    void put(String var1, String var2, Object var3);

    boolean contains(String var1, String var2);

    Object get(String var1, String var2);

    <T> T get(Class<T> var1, String var2, String var3);

    void remove(String var1, String var2);

    void clear(String var1);

    void clearAll();

    /** @deprecated */
    @Deprecated
    void notify(String var1, String var2);

    void put(String var1, String var2, Object var3, LocalParam var4);

    EhcacheFactory getLocalCacheFactory();
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.remote;

import java.util.Date;

public interface RemoteCacheService {
    void put(String var1, Object var2);

    void put(String var1, Object var2, Date var3);

    void put(String var1, Object var2, int var3);

    void putClient(String var1, String var2, Object var3);

    void putClient(String var1, String var2, Object var3, Date var4);

    void putClient(String var1, String var2, Object var3, int var4);

    Object get(String var1);

    <T> T get(Class<T> var1, String var2);

    Object getClient(String var1, String var2);

    <T> T getClient(String var1, Class<T> var2, String var3);

    void remove(String var1);

    void removeClient(String var1, String var2);

    void clear();

    void clearClient(String var1);

    void setDefaultClient(String var1);

    void destory();
}

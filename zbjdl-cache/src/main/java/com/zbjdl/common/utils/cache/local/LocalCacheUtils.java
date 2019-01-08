//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.local;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.CommonUtils;
import com.zbjdl.common.utils.cache.local.impl.EhcacheServiceImpl;
import com.zbjdl.common.utils.cache.local.param.LocalParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalCacheUtils {
    private static Logger logger = LoggerFactory.getLogger(LocalCacheUtils.class);
    private static boolean initialized = false;
    private static LocalCacheService localCacheService = (LocalCacheService)CommonUtils.newInstance("com.zbjdl.common.utils.cache.local.impl.NoneCacheServiceImpl");
    private static final String DEFAULT_LOCALCACHE_CONF_PATH = "classpath:../ehcachecfg.xml";

    public LocalCacheUtils() {
    }

    public static synchronized void init() {
        if (initialized()) {
            localCacheService = new EhcacheServiceImpl("_default_cache", localCacheService.getLocalCacheFactory(), "classpath:../ehcachecfg.xml");
        } else {
            localCacheService = new EhcacheServiceImpl("_default_cache", "classpath:../ehcachecfg.xml");
            initialized = true;
        }
    }

    public static boolean initialized() {
        return initialized && localCacheService != null && localCacheService.getLocalCacheFactory() != null;
    }

    public static void checkInitialized() {
        if (localCacheService == null) {
            throw new RuntimeException("LocalCacheUtils not initialized!");
        }
    }

    public static LocalCacheService createLocalCacheService(String name, String configPath) {
        checkInitialized();
        return new EhcacheServiceImpl("_default_cache", "config/ehcachecfg.xml");
    }

    public static void put(String cacheRegion, String key, Object value) {
        try {
            checkInitialized();
            CheckUtils.notEmpty(key, "key");
            localCacheService.put(cacheRegion, key, value);
        } catch (Throwable var4) {
            logger.error(" putting cache find out an error, cacheRegion:" + cacheRegion + " key:" + key + " value:" + value, var4);
        }

    }

    public static void put(String cacheRegion, String key, Object value, LocalParam localParam) {
        try {
            checkInitialized();
            CheckUtils.notEmpty(key, "key");
            localCacheService.put(cacheRegion, key, value, localParam);
        } catch (Throwable var5) {
            logger.error(" putting cache with localParam find out an error, cacheRegion:" + cacheRegion + " key:" + key + " value:" + value, var5);
        }

    }

    public static Object get(String cacheRegion, String key) {
        try {
            checkInitialized();
            CheckUtils.notEmpty(key, "key");
            return localCacheService.get(cacheRegion, key);
        } catch (Throwable var3) {
            logger.error(" getting cache find out an error, cacheRegion:" + cacheRegion + " key:" + key, var3);
            return null;
        }
    }

    public static <T> T get(Class<T> resultClazz, String cacheRegion, String key) {
        try {
            checkInitialized();
            CheckUtils.notEmpty(key, "key");
            CheckUtils.notEmpty(resultClazz, "resultClazz");
            return localCacheService.get(resultClazz, cacheRegion, key);
        } catch (Throwable var4) {
            logger.error(" getting cache with resultClazz find out an error, cacheRegion:" + cacheRegion + " key:" + key, var4);
            return null;
        }
    }

    public static void remove(String cacheRegion, String key) {
        try {
            checkInitialized();
            localCacheService.remove(cacheRegion, key);
        } catch (Throwable var3) {
            logger.error(" removing cache find out an error, cacheRegion:" + cacheRegion + " key:" + key, var3);
        }

    }

    public static void removeAndNotify(String cacheRegion, String cacheName, Object... keys) {
        try {
            checkInitialized();
            String key = generateCacheKey(cacheName, keys);
            localCacheService.remove(cacheRegion, key);
            notify(cacheRegion, key);
        } catch (Throwable var4) {
            logger.error(" removing and notifying cache find out an error, cacheRegion:" + cacheRegion + " cacheName:" + cacheName, var4);
        }

    }

    public static void clear(String cacheRegion) {
        try {
            checkInitialized();
            CheckUtils.notEmpty(cacheRegion, "cacheRegion");
            localCacheService.clear(cacheRegion);
        } catch (Throwable var2) {
            logger.error(" clearing cache find out an error, cacheRegion:" + cacheRegion, var2);
        }

    }

    public static void clearAll() {
        try {
            checkInitialized();
            localCacheService.clearAll();
        } catch (Throwable var1) {
            logger.error(" clearing all cache find out an error ", var1);
        }

    }

    /** @deprecated */
    @Deprecated
    public static void notify(String cacheRegion, String key) {
        checkInitialized();
        localCacheService.notify(cacheRegion, key);
    }

    public static boolean contains(String cacheRegion, String key) {
        checkInitialized();
        return localCacheService.contains(cacheRegion, key);
    }

    public static String generateCacheKey(String cacheName, Object... cacheKeys) {
        CheckUtils.notEmpty(cacheName, "cacheName");
        StringBuffer sb = new StringBuffer();
        sb.append(cacheName.hashCode());
        if (cacheKeys != null && cacheKeys.length != 0) {
            for(int i = 0; i < cacheKeys.length; ++i) {
                sb.append("_").append(cacheKeys[i] != null ? cacheKeys[i].hashCode() : "");
            }
        }

        return sb.toString();
    }
}

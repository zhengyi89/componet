package com.zbjdl.common.utils.cache.remote;

import com.zbjdl.common.encrypt.Digest;
import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.cache.local.LocalCacheUtils;
import com.zbjdl.common.utils.cache.remote.impl.NoneCacheServiceImpl;
import com.zbjdl.common.utils.cache.remote.impl.RedisServiceImpl;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteCacheUtils {
    private static boolean initialized = false;
    private static RemoteCacheService service = new NoneCacheServiceImpl();
    private static final Integer KEY_LENGTH = Integer.valueOf(81);
    private static Logger logger = LoggerFactory.getLogger(RemoteCacheUtils.class);

    private RemoteCacheUtils() {
    }

    public static synchronized void init() {
        if (!initialized()) {
            service = new RedisServiceImpl("defaultClient");
            initialized = true;
        }
    }

    public static boolean initialized() {
        return initialized && service != null;
    }

    public static void put(String key, Object value) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            checkIsNULL(value, "value");
            key = checkKeyLength(key);
            service.put(key, value);
        } catch (Throwable var3) {
            logger.error(" putting cache find out an error, key:" + key + " value:" + value, var3);
        }

    }

    public static void put(String key, Object value, Date expireDate) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            CheckUtils.valueIsNull(expireDate, "expireDate");
            checkIsNULL(value, "value");
            key = checkKeyLength(key);
            service.put(key, value, expireDate);
        } catch (Throwable var4) {
            logger.error(" putting cache find out an error, key:" + key + " value:" + value + " expireDate:" + expireDate, var4);
        }

    }

    public static void put(String key, Object value, int timeToLive) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            checkIsNULL(value, "value");
            key = checkKeyLength(key);
            service.put(key, value, timeToLive);
        } catch (Throwable var4) {
            logger.error(" putting cache find out an error, key:" + key + " value:" + value + " timeToLive:" + timeToLive, var4);
        }

    }

    public static void putClient(String name, String key, Object value) {
        try {
            CheckUtils.valueIsEmpty(new String[]{name, key}, new String[]{"name", "key"});
            checkIsNULL(value, "value");
            key = checkKeyLength(key);
            service.putClient(name, key, value);
        } catch (Throwable var4) {
            logger.error(" putting clientCache find out an error, key:" + key + " value:" + value + " name:" + name, var4);
        }

    }

    public static void putClient(String name, String key, Object value, Date expireDate) {
        try {
            CheckUtils.valueIsEmpty(new String[]{name, key}, new String[]{"name", "key"});
            CheckUtils.valueIsNull(expireDate, "expireDate");
            checkIsNULL(value, "value");
            key = checkKeyLength(key);
            service.putClient(name, key, value, expireDate);
        } catch (Throwable var5) {
            logger.error(" putting clientCache find out an error, key:" + key + " value:" + value + " name:" + name + " expireDate:" + expireDate, var5);
        }

    }

    public static void putClient(String name, String key, Object value, int timeToLive) {
        try {
            CheckUtils.valueIsEmpty(new String[]{name, key}, new String[]{"name", "key"});
            checkIsNULL(value, "value");
            key = checkKeyLength(key);
            service.putClient(name, key, value, timeToLive);
        } catch (Throwable var5) {
            logger.error(" putting clientCache find out an error, key:" + key + " value:" + value + " name:" + name + " timeToLive:" + timeToLive, var5);
        }

    }

    public static Object get(String key) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            key = checkKeyLength(key);
            return service.get(key);
        } catch (Throwable var2) {
            logger.error(" getting cache find out an error, key:" + key, var2);
            return null;
        }
    }

    public static <T> T get(Class<T> clazz, String key) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            checkIsNULL(clazz, "clazz");
            key = checkKeyLength(key);
            return service.get(clazz, key);
        } catch (Throwable var3) {
            logger.error(" getting clazzCache find out an error, key:" + key, var3);
            return null;
        }
    }

    public static Object getClient(String name, String key) {
        try {
            CheckUtils.valueIsEmpty(new String[]{name, key}, new String[]{"name", "key"});
            key = checkKeyLength(key);
            return service.getClient(name, key);
        } catch (Throwable var3) {
            logger.error(" getting clientCache find out an error, key:" + key + " name:" + name, var3);
            return null;
        }
    }

    public static <T> T getClient(String name, Class<T> clazz, String key) {
        try {
            CheckUtils.valueIsEmpty(new String[]{name, key}, new String[]{"name", "key"});
            checkIsNULL(clazz, "clazz");
            key = checkKeyLength(key);
            return service.getClient(name, clazz, key);
        } catch (Throwable var4) {
            logger.error(" getting clientClazzCache find out an error, key:" + key + " name:" + name, var4);
            return null;
        }
    }

    public static void remove(String key) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            key = checkKeyLength(key);
            service.remove(key);
        } catch (Throwable var2) {
            logger.error(" removing cache find out an error, key:" + key, var2);
        }

    }

    public static void remove(String cacheName, Object... cacheKeys) {
        try {
            CheckUtils.valueIsEmpty(cacheName, "cacheName");
            String key = generateCacheKey(cacheName, cacheKeys);
            remove(key);
        } catch (Throwable var3) {
            logger.error(" removing cache by generateCacheKey find out an error, cacheName:" + cacheName, var3);
        }

    }

    public static void removeClient(String name, String key) {
        try {
            CheckUtils.valueIsEmpty(new String[]{name, key}, new String[]{"name", "key"});
            key = checkKeyLength(key);
            service.removeClient(name, key);
        } catch (Throwable var3) {
            logger.error(" removing clientCache find out an error, name:" + name + " key:" + key, var3);
        }

    }

    public static void clear() {
        try {
            service.clear();
        } catch (Throwable var1) {
            logger.error(" clearing cache find out an error ", var1);
        }

    }

    public static void clearClient(String name) {
        try {
            CheckUtils.valueIsEmpty(name, "name");
            service.clearClient(name);
        } catch (Throwable var2) {
            logger.error(" clearing clientCache find out an error ", var2);
        }

    }

    private static String checkKeyLength(String key) {
        if (key.getBytes().length > KEY_LENGTH.intValue()) {
            key = Digest.md5Digest(key);
        }

        return key;
    }

    public static Class getCacheType() {
        return service.getClass();
    }

    public static String generateCacheKey(String cacheName, Object... cacheKeys) {
        return LocalCacheUtils.generateCacheKey(cacheName, cacheKeys);
    }

    private static void checkIsNULL(Object value, String msg) {
        if (value == null) {
            throw new RuntimeException(msg + " is not null");
        }
    }
}

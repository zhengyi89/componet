package com.zbjdl.common.utils.cache.remote.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.zbjdl.common.encrypt.Digest;
import com.zbjdl.common.json.JsonMapper;
import com.zbjdl.common.redis.RedisClientUtils;
import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.DateUtils;
import com.zbjdl.common.utils.cache.local.LocalCacheUtils;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisCacheUtils {
    private static boolean initialized = false;
    private static final Integer KEY_LENGTH = Integer.valueOf(81);
    private static Logger logger = LoggerFactory.getLogger(RedisCacheUtils.class);

    private RedisCacheUtils() {
    }

    public static synchronized void init() {
        if (!initialized()) {
            RedisClientUtils.init();
            initialized = true;
        }
    }

    public static boolean initialized() {
        return initialized;
    }

    private static String objectToString(Object object) {
        JsonMapper mapper = new JsonMapper();
        return mapper.toJson(object);
    }

    public static void put(String key, Object value) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            checkIsNULL(value, "value");
            key = checkKeyLength(key);
            String jsonValue = objectToString(value);
            RedisClientUtils.getRedisTemplate().set(key, jsonValue);
        } catch (Throwable var3) {
            logger.error(" putting cache find out an error, key:" + key + " value:" + value, var3);
        }

    }

    public static void put(String key, Object value, Date expireDate) {
        Long timeToLive = DateUtils.getDiffSeconds(expireDate, new Date());
        put(key, value, timeToLive.intValue());
    }

    public static void put(String key, Object value, int timeToLive) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            checkIsNULL(value, "value");
            key = checkKeyLength(key);
            String jsonValue = objectToString(value);
            RedisClientUtils.getRedisTemplate().setex(key, timeToLive, jsonValue);
        } catch (Throwable var4) {
            logger.error(" putting cache find out an error, key:" + key + " value:" + value + " timeToLive:" + timeToLive, var4);
        }

    }

    public static Object get(String key) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            key = checkKeyLength(key);
            String jsonValue = RedisClientUtils.getRedisTemplate().get(key);
            return jsonValue;
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
            String jsonValue = RedisClientUtils.getRedisTemplate().get(key);
            return JsonMapper.nonDefaultMapper().fromJson(jsonValue, clazz);
        } catch (Throwable var3) {
            logger.error(" getting clazzCache find out an error, key:" + key, var3);
            return null;
        }
    }

    public static <T> T get(JavaType javaType, String key) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            checkIsNULL(javaType, "clazz");
            key = checkKeyLength(key);
            String jsonValue = RedisClientUtils.getRedisTemplate().get(key);
            return JsonMapper.nonDefaultMapper().fromJson(jsonValue, javaType);
        } catch (Throwable var3) {
            logger.error(" getting clazzCache find out an error, key:" + key, var3);
            return null;
        }
    }

    public static void remove(String key) {
        try {
            CheckUtils.valueIsEmpty(key, "key");
            key = checkKeyLength(key);
            RedisClientUtils.getRedisTemplate().del(key);
        } catch (Throwable var2) {
            logger.error(" removing cache find out an error, key:" + key, var2);
        }

    }

    private static String checkKeyLength(String key) {
        if (key.getBytes().length > KEY_LENGTH.intValue()) {
            key = Digest.md5Digest(key);
        }

        return key;
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

package com.zbjdl.common.utils.cache.remote.impl;

import com.zbjdl.common.utils.cache.remote.RemoteCacheService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisServiceImpl implements RemoteCacheService {
    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    private static final String KEY_PREFIX = "remotecache:";
    private String defaultClient = "defaultClient";

    public static synchronized void initCacheManager() {
        try {
            RedisCacheUtils.init();
        } catch (Exception var1) {
            logger.error(var1.getMessage(), var1);
        }

    }

    private String generateClientKey() {
        return this.generateClientKey(this.defaultClient);
    }

    private String generateClientKey(String clientName) {
        StringBuffer buffer = (new StringBuffer()).append("remotecache:").append(":").append(clientName);
        return buffer.toString();
    }

    private String generateKey(String key) {
        return this.generateKey(this.defaultClient, key);
    }

    private String generateKey(String clientName, String key) {
        StringBuffer buffer = (new StringBuffer()).append(this.generateClientKey(clientName)).append(":").append(key);
        return buffer.toString();
    }

    public RedisServiceImpl(String clientCacheName) {
        initCacheManager();
        this.defaultClient = clientCacheName;
    }

    public void destory() {
    }

    public void put(String key, Object value) {
        RedisCacheUtils.put(this.generateKey(key), value);
    }

    public void put(String key, Object value, Date expireDate) {
        RedisCacheUtils.put(this.generateKey(key), value);
    }

    public void put(String key, Object value, int timeToLive) {
        RedisCacheUtils.put(this.generateKey(key), value, timeToLive);
    }

    public void putClient(String name, String key, Object value) {
        RedisCacheUtils.put(this.generateKey(name, key), value);
    }

    public void putClient(String name, String key, Object value, Date expireDate) {
        RedisCacheUtils.put(this.generateKey(name, key), value, expireDate);
    }

    public void putClient(String name, String key, Object value, int timeToLive) {
        RedisCacheUtils.put(this.generateKey(name, key), value, timeToLive);
    }

    public Object get(String key) {
        return RedisCacheUtils.get(this.generateKey(key));
    }

    public <T> T get(Class<T> clazz, String key) {
        return RedisCacheUtils.get(clazz, this.generateKey(key));
    }

    public Object getClient(String name, String key) {
        return RedisCacheUtils.get(this.generateKey(name, key));
    }

    public <T> T getClient(String name, Class<T> clazz, String key) {
        return RedisCacheUtils.get(clazz, this.generateKey(name, key));
    }

    public void remove(String key) {
        RedisCacheUtils.remove(this.generateKey(key));
    }

    public void removeClient(String clientName, String key) {
        RedisCacheUtils.remove(this.generateKey(clientName, key));
    }

    public void clear() {
        RedisCacheUtils.remove(this.generateClientKey());
    }

    public void clearClient(String clientName) {
        RedisCacheUtils.remove(this.generateClientKey(clientName));
    }

    public void setDefaultClient(String clientName) {
        this.defaultClient = clientName;
    }
}

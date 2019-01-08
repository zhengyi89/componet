package com.zbjdl.common.utils.cache.local.impl;

import com.zbjdl.common.utils.cache.local.LocalCacheService;
import com.zbjdl.common.utils.cache.local.param.LocalParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoneCacheServiceImpl implements LocalCacheService {
    private static final Logger logger = LoggerFactory.getLogger(NoneCacheServiceImpl.class);

    public NoneCacheServiceImpl() {
    }

    public void put(String cacheRegion, String key, Object value) {
        logger.warn("local cache not initialized, put action has no effect");
    }

    public boolean contains(String cacheRegion, String key) {
        logger.warn("local cache not initialized, contains action has no effect");
        return false;
    }

    public Object get(String cacheRegion, String key) {
        logger.warn("local cache not initialized, get action has no effect");
        return null;
    }

    public <T> T get(Class<T> resultClazz, String cacheRegion, String key) {
        logger.warn("local cache not initialized, get action has no effect");
        return null;
    }

    public void remove(String cacheRegion, String key) {
        logger.warn("local cache not initialized, remove action has no effect");
    }

    public void clear(String cacheRegion) {
        logger.warn("local cache not initialized, clear action has no effect");
    }

    public void clearAll() {
        logger.warn("local cache not initialized, clearAll action has no effect");
    }

    public void notify(String cacheRegion, String key) {
        logger.warn("local cache not initialized, notify action has no effect");
    }

    public EhcacheFactory getLocalCacheFactory() {
        logger.warn("local cache not initialized, notify action has no effect");
        return null;
    }

    public void put(String cacheRegion, String key, Object value, LocalParam localParam) {
        logger.warn("local cache not initialized, notify action has no effect");
    }
}

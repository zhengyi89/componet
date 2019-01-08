package com.zbjdl.common.utils.cache.remote.impl;

import com.zbjdl.common.utils.cache.remote.RemoteCacheService;
import java.util.Date;

public class NoneCacheServiceImpl implements RemoteCacheService {
    public NoneCacheServiceImpl() {
    }

    public void put(String key, Object value) {
    }

    public void put(String key, Object value, Date expireDate) {
    }

    public void put(String key, Object value, int timeToLive) {
    }

    public void putClient(String name, String key, Object value) {
    }

    public void putClient(String name, String key, Object value, Date expireDate) {
    }

    public void putClient(String name, String key, Object value, int timeToLive) {
    }

    public Object get(String key) {
        return null;
    }

    public <T> T get(Class<T> clazz, String key) {
        return null;
    }

    public Object getClient(String name, String key) {
        return null;
    }

    public <T> T getClient(String name, Class<T> clazz, String key) {
        return null;
    }

    public void remove(String key) {
    }

    public void removeClient(String name, String key) {
    }

    public void clear() {
    }

    public void clearClient(String name) {
    }

    public void setDefaultClient(String name) {
    }

    public void destory() {
    }
}

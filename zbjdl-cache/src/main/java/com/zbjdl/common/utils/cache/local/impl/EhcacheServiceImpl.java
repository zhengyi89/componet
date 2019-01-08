//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.local.impl;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.cache.local.LocalCacheService;
import com.zbjdl.common.utils.cache.local.param.LocalParam;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EhcacheServiceImpl implements LocalCacheService {
    private static final Logger logger = LoggerFactory.getLogger(EhcacheServiceImpl.class);
    public static String CACHE_NOTIFY_PREFIX = "_yeepay_cache_update_";
    private String name;
    private EhcacheFactory ehcacheFactory;

    public EhcacheServiceImpl(String name, String configPath) {
        this.name = name;
        this.ehcacheFactory = EhcacheFactory.createEhcacheFactory(configPath);
        this.notify(name);
    }

    public EhcacheServiceImpl(String name, EhcacheFactory ehcacheFactory, String configPath) {
        this.name = name;
        this.ehcacheFactory = EhcacheFactory.addEhcacheIntoFactory(configPath, ehcacheFactory.getCaches(), ehcacheFactory.getManagers());
        this.notify(name);
    }

    /** @deprecated */
    @Deprecated
    private void notify(String cacheName) {
    }

    /** @deprecated */
    @Deprecated
    public void notify(String cacheRegion, String key) {
        cacheRegion = cacheRegion == null ? "" : cacheRegion;
        key = key == null ? "" : key;
        logger.debug("send cache notify event, msgName:" + CACHE_NOTIFY_PREFIX + this.name + ", msgValue:" + cacheRegion + "|" + key);
    }

    private Ehcache getCache(String cacheRegion) {
        return this.getCache(cacheRegion, true);
    }

    private synchronized Ehcache getCache(String cacheRegion, boolean isNew) {
        if (cacheRegion != null && cacheRegion.trim().length() != 0) {
            if (cacheRegion.indexOf("|") >= 0) {
                throw new IllegalArgumentException("cacheRegion must not contains '|'");
            } else {
                return this.ehcacheFactory.createEhcache(cacheRegion, isNew);
            }
        } else {
            return this.ehcacheFactory.createDefaultEhcache();
        }
    }

    public void put(String cacheRegion, String key, Object value) {
        CheckUtils.strNotNull(key, "key");
        this.getCache(cacheRegion).put(new Element(key, value));
    }

    public void put(String cacheRegion, String key, Object value, LocalParam localParam) {
        CheckUtils.strNotNull(key, "key");
        this.getCache(cacheRegion).put(new Element(key, value, localParam.isEternal(), localParam.getTimeToIdleSeconds(), localParam.getTimeToLiveSeconds()));
    }

    public boolean contains(String cacheRegion, String key) {
        CheckUtils.strNotNull(key, "key");
        Element e = this.getCache(cacheRegion).get(key);
        return e != null;
    }

    public Object get(String cacheRegion, String key) {
        CheckUtils.strNotNull(key, "key");
        Element e = this.getCache(cacheRegion).get(key);
        return e != null ? e.getObjectValue() : null;
    }

    public <T> T get(Class<T> resultClazz, String cacheRegion, String key) {
        return (T) this.get(cacheRegion, key);
    }

    public void remove(String cacheRegion, String key) {
        CheckUtils.strNotNull(key, "key");
        Ehcache cache = this.getCache(cacheRegion, false);
        if (cache != null) {
            cache.remove(key);
        }

    }

    public void clear(String cacheRegion) {
        Ehcache cache = this.getCache(cacheRegion, false);
        if (cache != null) {
            cache.removeAll();
        }

    }

    public EhcacheFactory getLocalCacheFactory() {
        return this.ehcacheFactory;
    }

    public void clearAll() {
        this.ehcacheFactory.clearAll();
    }
}

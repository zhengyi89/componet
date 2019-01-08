//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.local.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

public class EhcacheFactory {
    private static final Logger logger = LoggerFactory.getLogger(EhcacheFactory.class);
    private Map<String, CacheManager> managers;
    private Map<String, Cache> caches;
    private static final String DEFAULT_APP_CACHE_CFG_NAME = "default_yeepay_cache_ehcachecfg";
    private static final String DEFAULT_APP_CACHE_URL = "default_ehcachecfg.xml";
    private static final String DEFAULT_APP_MANAGER_CFG_NAME = "default_yeepay_manager_ehcachecfg";

    private EhcacheFactory(Map<String, Cache> caches, Map<String, CacheManager> managers) {
        this.caches = caches;
        this.managers = managers;
    }

    public static EhcacheFactory createEhcacheFactory(String configPath) {
        Map[] maps = putEhcacheCacheRegion(configPath, (Map)null, (Map)null);
        EhcacheFactory ehcacheFactory = new EhcacheFactory(maps[0], maps[1]);
        return ehcacheFactory;
    }

    public static EhcacheFactory addEhcacheIntoFactory(String configPath, Map<String, Cache> caches, Map<String, CacheManager> managers) {
        Map[] maps = putEhcacheCacheRegion(configPath, caches, managers);
        EhcacheFactory ehcacheFactory = new EhcacheFactory(maps[0], maps[1]);
        return ehcacheFactory;
    }

    public Ehcache createDefaultEhcache() {
        return (Ehcache)this.caches.get("default_yeepay_cache_ehcachecfg");
    }

    public Ehcache createEhcache(String cacheRegionName, Boolean isNew) {
        Ehcache cache = (Ehcache)this.caches.get(cacheRegionName);
        if (cache == null && isNew.booleanValue()) {
            ((CacheManager)this.managers.get("default_yeepay_manager_ehcachecfg")).addCache(cacheRegionName);
            this.caches.put(cacheRegionName, ((CacheManager)this.managers.get("default_yeepay_manager_ehcachecfg")).getCache(cacheRegionName));
            return (Ehcache)this.caches.get("default_yeepay_cache_ehcachecfg");
        } else {
            if (cache == null) {
                logger.warn("create ehcache fail, cacheRegion : " + cacheRegionName);
            }

            return cache;
        }
    }

    public void clearAll() {
        Iterator i$ = this.managers.entrySet().iterator();

        while(i$.hasNext()) {
            Entry<String, CacheManager> m = (Entry)i$.next();
            ((CacheManager)m.getValue()).clearAll();
        }

    }

    private static List<URL> toFindUrl(String configPath) {
        List<URL> list = new ArrayList();
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext();

        try {
            Resource[] defaultResource = appContext.getResources("classpath*:default_ehcachecfg.xml");
            Resource resource = appContext.getResource(configPath);
            Resource[] resources = appContext.getResources("classpath*:ehcachecfg.xml");
            if (resource.exists()) {
                list.add(resource.getURL());
            }

            Resource[] arr$ = resources;
            int len$ = resources.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Resource r = arr$[i$];
                if (r.exists()) {
                    list.add(r.getURL());
                }
            }

            if (defaultResource != null && defaultResource.length != 0 && defaultResource[0].getURL() != null) {
                list.add(defaultResource[0].getURL());
            } else {
                logger.error(" creating default cache error, beacuse cannot find out path ");
            }
        } catch (IOException var10) {
            logger.error(" this app find IOException when it lookup for url  : " + configPath + " ; IOException : ", var10);
        }

        return list;
    }

    private static Map[] putEhcacheCacheRegion(String configPath, Map<String, Cache> caches, Map<String, CacheManager> managers) {
        if (caches == null) {
            caches = new HashMap();
        }

        if (managers == null) {
            managers = new HashMap();
        }

        List<URL> list = toFindUrl(configPath);
        logger.info("ehcache file list : " + list);
        Iterator i$ = list.iterator();

        while(true) {
            while(i$.hasNext()) {
                URL u = (URL)i$.next();
                CacheManager c = new CacheManager(u);
                if (c.getCacheNames() == null) {
                    logger.error(" Incorrect configuration file! To create cachemanager is error! url : " + u);
                } else {
                    if (u.toString().contains("default_ehcachecfg.xml")) {
                        ((Map)managers).put("default_yeepay_manager_ehcachecfg", c);
                    } else {
                        if (((Map)managers).containsKey(u.toString())) {
                            logger.warn(" warning to create CacheManager in the different app! this CacheManager has the duplication of name! the name is  " + u.toString());
                            continue;
                        }

                        ((Map)managers).put(u.toString(), c);
                    }

                    String[] arr$ = c.getCacheNames();
                    int len$ = arr$.length;

                    for(int i = 0; i < len$; ++i) {
                        String s = arr$[i];
                        if (s == null) {
                            logger.error(" Incorrect configuration file! the CacheName is null! url : " + u);
                        } else if (((Map)caches).containsKey(s)) {
                            logger.warn(" warning configuration files in the same app! this cache has the duplication of name! the name is  " + s + " url : " + u);
                        } else {
                            ((Map)caches).put(s, c.getCache(s));
                        }
                    }
                }
            }

            return new Map[]{(Map)caches, (Map)managers};
        }
    }

    public Map<String, CacheManager> getManagers() {
        return this.managers;
    }

    public Map<String, Cache> getCaches() {
        return this.caches;
    }
}

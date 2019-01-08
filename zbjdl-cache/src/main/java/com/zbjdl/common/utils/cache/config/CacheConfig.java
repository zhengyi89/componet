//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.config;

import java.util.List;

public class CacheConfig {
    private String name;
    private String cacheRegion;
    private List<CacheKeyConfig> cacheKeyConfigs;
    private boolean isUpdate;
    private CacheTypeEnum type;
    private int time;
    private boolean eternal;
    private int timeToIdleSeconds;
    private int timeToLiveSeconds;

    public CacheConfig(String name, String cacheRegion, List<CacheKeyConfig> cacheKeyConfigs, boolean isUpdate) {
        this.name = name;
        this.cacheRegion = cacheRegion;
        this.cacheKeyConfigs = cacheKeyConfigs;
        this.isUpdate = isUpdate;
    }

    public CacheConfig(String name, String cacheRegion, List<CacheKeyConfig> cacheKeyConfigs, boolean isUpdate, CacheTypeEnum type, int time, boolean eternal, int timeToIdleSeconds, int timeToLiveSeconds) {
        this.name = name;
        this.cacheRegion = cacheRegion;
        this.cacheKeyConfigs = cacheKeyConfigs;
        this.isUpdate = isUpdate;
        this.type = type;
        this.time = time;
        this.eternal = eternal;
        this.timeToIdleSeconds = timeToIdleSeconds;
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    public int getTime() {
        return this.time;
    }

    public boolean isEternal() {
        return this.eternal;
    }

    public int getTimeToIdleSeconds() {
        return this.timeToIdleSeconds;
    }

    public int getTimeToLiveSeconds() {
        return this.timeToLiveSeconds;
    }

    public String getName() {
        return this.name;
    }

    public String getCacheRegion() {
        return this.cacheRegion;
    }

    public List<CacheKeyConfig> getCacheKeyConfigs() {
        return this.cacheKeyConfigs;
    }

    public boolean isUpdate() {
        return this.isUpdate;
    }

    public CacheTypeEnum getType() {
        return this.type;
    }
}

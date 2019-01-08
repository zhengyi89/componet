package com.zbjdl.common.utils.config.impl;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.CommonUtils;
import com.zbjdl.common.utils.config.ConfigNotifyListener;
import com.zbjdl.common.utils.config.ConfigParam;
import com.zbjdl.common.utils.config.ConfigParamGroup;
import com.zbjdl.common.utils.config.ConfigRepository;
import com.zbjdl.common.utils.config.facade.ConfigQueryFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfigRepository implements ConfigRepository {
    private static final Logger logger = LoggerFactory.getLogger(DefaultConfigRepository.class);
    private final Map<String, Map<String, Object>> configParams = new HashMap();
    private final Map<String, Integer> versions = new HashMap();
    private ConfigQueryFacade configQueryFacade;
    private String configNamespace = "default";
    private long refreshPeriod = 60000L;
    private String loadTyes;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock;
    private final Lock writeLock;
    private final List<ConfigNotifyListener> notifyList;

    public DefaultConfigRepository() {
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.notifyList = new ArrayList();
    }

    public void init() {
        logger.info("init LocalCacheConfigRepository start");
        Map<String, String> props = CommonUtils.loadProps("config/application.properties");
        logger.info("loadProps : " + props);
        String namespace = (String)props.get("zbjdl.config.namespace");
        if (!CheckUtils.isEmpty(namespace)) {
            this.configNamespace = namespace;
        }

        String refresh = (String)props.get("zbjdl.config.refresh");
        if (refresh != null) {
            this.refreshPeriod = Long.parseLong(refresh);
        }

        this.loadTyes = (String)props.get("zbjdl.config.loadTyes");
        if (CheckUtils.isEmpty(this.loadTyes)) {
            this.loadAllConfig();
        } else {
            String[] loadTypes = this.loadTyes.split(",");
            this.loadConfig(loadTypes);
        }

        this.startRefreshDaemon();
        logger.info("init LocalCacheConfigRepository success, from local configNamespace:" + this.configNamespace + " refreshPeriod:" + this.refreshPeriod + " loadTypes:" + this.loadTyes);
    }

    public void loadConfig(String... types) {
        logger.info("loadConfig" + types);
        CheckUtils.notEmpty(types, "loadTypes");
        String[] var2 = types;
        int var3 = types.length;

        String key;
        for(int var4 = 0; var4 < var3; ++var4) {
            key = var2[var4];
            CheckUtils.notEmpty(key, "loadType");
        }

        Map<String, Integer> loadVersions = this.configQueryFacade.queryConfigVersion(this.configNamespace, types);
        Map<String, Map<String, Object>> loadParams = this.configQueryFacade.loadConfig(this.configNamespace, types);
        if (loadVersions != null && loadParams != null) {
            this.writeLock.lock();

            Iterator var12;
            try {
                logger.info("load config types : " + loadVersions.keySet());
                var12 = loadVersions.keySet().iterator();

                while(true) {
                    if (!var12.hasNext()) {
                        this.versions.putAll(loadVersions);
                        this.configParams.putAll(loadParams);
                        break;
                    }

                    key = (String)var12.next();
                    this.versions.remove(key);
                    Map m = (Map)this.configParams.remove(key);
                    if (m != null) {
                        m.clear();
                    }
                }
            } finally {
                this.writeLock.unlock();
            }

            var12 = this.notifyList.iterator();

            while(var12.hasNext()) {
                ConfigNotifyListener listener = (ConfigNotifyListener)var12.next();
                listener.notifyConfig(this.configParams);
            }

        } else {
            logger.error("config and version must be not null, version[" + loadVersions + "] config[" + loadParams + "]");
        }
    }

    public void loadAllConfig() {
        Map<String, Integer> loadVersions = this.configQueryFacade.queryConfigVersion(this.configNamespace, new String[0]);
        Map<String, Map<String, Object>> loadParams = this.configQueryFacade.loadConfig(this.configNamespace, new String[0]);
        if (loadParams != null && loadVersions != null) {
            this.versions.clear();
            this.versions.putAll(loadVersions);
            this.writeLock.lock();

            try {
                logger.info("load all config types : " + loadVersions.keySet());
                Iterator var3 = this.configParams.values().iterator();

                while(var3.hasNext()) {
                    Map<String, Object> value = (Map)var3.next();
                    value.clear();
                }

                this.configParams.clear();
                this.configParams.putAll(loadParams);
            } finally {
                this.writeLock.unlock();
            }
        } else {
            logger.debug("no config param loaded! loadParams[" + loadParams + "] loadVersions[" + loadVersions + "]");
        }
    }

    public void refreshConfig() {
        logger.debug("refreshConfig start......");
        String[] loadTypes = new String[this.versions.keySet().size()];
        this.versions.keySet().toArray(loadTypes);
        Map<String, Integer> loadVersions = this.configQueryFacade.queryConfigVersion(this.configNamespace, new String[0]);
        if (loadVersions != null && !loadVersions.isEmpty()) {
            logger.debug("query version : " + loadVersions);
            List<String> tmpReload = new ArrayList();
            Iterator var4 = this.versions.keySet().iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                if (loadVersions.containsKey(key)) {
                    Integer oldVersion = (Integer)this.versions.get(key);
                    if (oldVersion == null) {
                        oldVersion = Integer.valueOf(-1);
                    }

                    Integer newVersion = (Integer)loadVersions.remove(key);
                    if (newVersion == null) {
                        newVersion = Integer.valueOf(-1);
                    }

                    if (!oldVersion.equals(newVersion)) {
                        tmpReload.add(key);
                    }
                }
            }

            tmpReload.addAll(loadVersions.keySet());
            if (!tmpReload.isEmpty()) {
                logger.info("reload config types : " + tmpReload);
                String[] reloadTypes = new String[tmpReload.size()];
                tmpReload.toArray(reloadTypes);
                this.loadConfig(reloadTypes);
            }
        } else {
            if (loadTypes.length > 0) {
                logger.error("can't load config version, configTypes : " + this.versions.keySet());
            }

        }
    }

    private void startRefreshDaemon() {
        Thread refreshDaemon = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(DefaultConfigRepository.this.refreshPeriod);
                    } catch (InterruptedException var3) {
                        return;
                    }

                    try {
                        DefaultConfigRepository.this.refreshConfig();
                    } catch (Exception var2) {
                        DefaultConfigRepository.logger.warn("refreshConfig fail:" + var2.getMessage(), var2);
                    }
                }
            }
        });
        refreshDaemon.setDaemon(true);
        refreshDaemon.start();
    }

    public ConfigParamGroup getConfigGroup(String configType) {
        CheckUtils.notEmpty(configType, "configType");
        return new DefaultConfigParamGroup(configType, this.readLock, this.configParams);
    }

    public ConfigParam<?> getConfig(String configType, String configKey) {
        CheckUtils.notEmpty(configKey, "configKey");
        CheckUtils.notEmpty(configType, "configType");
        return new DefaultConfigParam(configType, configKey, this.readLock, this.configParams);
    }

    public void addListener(ConfigNotifyListener listener) {
        this.notifyList.add(listener);
        logger.error("DefaultConfigRepository的listener当前个数为：{}", this.notifyList.size());
    }

    public void setConfigQueryFacade(ConfigQueryFacade configQueryFacade) {
        this.configQueryFacade = configQueryFacade;
    }

    public String getConfigNamespace() {
        return this.configNamespace;
    }

    public void setConfigNamespace(String configNamespace) {
        this.configNamespace = configNamespace;
    }

    public long getRefreshPeriod() {
        return this.refreshPeriod;
    }

    public void setRefreshPeriod(long refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public String getLoadTyes() {
        return this.loadTyes;
    }

    public void setLoadTyes(String loadTyes) {
        this.loadTyes = loadTyes;
    }
}

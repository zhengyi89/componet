package com.zbjdl.common.utils.config.service.impl;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.config.dao.ConfigDao;
import com.zbjdl.common.utils.config.dao.ConfigNamespaceDao;
import com.zbjdl.common.utils.config.dao.ConfigTypeDao;
import com.zbjdl.common.utils.config.dao.ConfigVersionDao;
import com.zbjdl.common.utils.config.entity.Config;
import com.zbjdl.common.utils.config.entity.ConfigNamespace;
import com.zbjdl.common.utils.config.entity.ConfigType;
import com.zbjdl.common.utils.config.entity.ConfigVersion;
import com.zbjdl.common.utils.config.service.ConfigService;
import com.zbjdl.common.utils.config.utils.ConfigUtil;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class ConfigServiceImpl implements ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private ConfigDao configDao;
    private ConfigVersionDao configVersionDao;
    private ConfigNamespaceDao configNamespaceDao;
    private ConfigTypeDao configTypeDao;

    public ConfigServiceImpl() {
    }

    @Transactional
    public void saveOrUpdateConfig(Config config) {
        CheckUtils.valueIsNull(config, "namespace,type,configKey,valueType,value,updatable,deletable");
        String key = config.getConfigKey().trim();
        String namespace = config.getNamespace().trim();
        String type = config.getType().trim();
        Config cf = new Config();
        cf.setNamespace(namespace);
        cf.setType(type);
        cf.setConfigKey(key);
        List<Config> list = this.configDao.queryConfig(cf);
        Config cfg = null;
        if (list != null && list.size() > 0) {
            cfg = (Config)list.get(0);
        }

        if (cfg != null) {
            if (!cfg.getUpdatable().booleanValue()) {
                throw new RuntimeException("配置不能修改");
            }

            if (cfg.getConfigKey() != null) {
                config.setId(cfg.getId());
                config.setUpdateDate(new Date());
            }

            this.configDao.update(config);
            this.updateVersion(config);
        } else {
            config.setConfigKey(key);
            config.setNamespace(namespace);
            config.setType(type);
            config.setCreateDate(new Date());
            this.configDao.add(config);
            this.updateVersion(config);
        }

    }

    @Transactional
    public Long createConfig(Config config) {
        CheckUtils.valueIsNull(config, "namespace,type,configKey,valueType,value,updatable,deletable");
        if (this.configIsOne(config).booleanValue()) {
            config.setConfigKey(config.getConfigKey().trim());
            config.setNamespace(config.getNamespace().trim());
            config.setType(config.getType().trim());
            config.setCreateDate(new Date());
            this.configDao.add(config);
            this.updateVersion(config);
            return config.getId();
        } else {
            throw new RuntimeException("命名空间为  " + config.getNamespace() + ", 类型为 " + config.getType() + ", 键为 " + config.getConfigKey() + " 的配置已存在 ");
        }
    }

    private Boolean configIsOne(Config config) {
        String key = config.getConfigKey().trim();
        String namespace = config.getNamespace().trim();
        String type = config.getType().trim();
        Config cfg = this.configDao.queryConfigByKey(namespace, type, key);
        return cfg != null ? false : true;
    }

    @Transactional
    public void changeConfig(Config config) {
        CheckUtils.valueIsNull(config, "id");
        Config cfg = (Config)this.configDao.get(config.getId());
        if (cfg != null) {
            if (!cfg.getUpdatable().booleanValue()) {
                throw new RuntimeException("配置不能修改");
            }

            if (cfg.getConfigKey() != null) {
                config.setConfigKey(config.getConfigKey().trim());
                this.validateUpdate(cfg, config);
                config.setUpdateDate(new Date());
            }

            this.configDao.update(config);
            this.updateVersion(cfg);
        }

    }

    private Boolean validateUpdate(Config DBcfg, Config config) {
        String key;
        String namespace;
        String type;
        Config cfg;
        if (config.getNamespace() != null && config.getType() != null) {
            key = config.getConfigKey().trim();
            namespace = config.getNamespace().trim();
            type = config.getType().trim();
            cfg = this.configDao.queryConfigByKey(namespace, type, key);
            if (cfg != null && !cfg.getId().equals(DBcfg.getId())) {
                throw new RuntimeException("命名空间为  " + namespace + ", 类型为 " + type + ", 键为 " + key + " 的配置已存在 ");
            }
        } else if (config.getNamespace() != null && config.getType() == null) {
            key = config.getConfigKey().trim();
            namespace = config.getNamespace().trim();
            type = DBcfg.getType().trim();
            cfg = this.configDao.queryConfigByKey(namespace, type, key);
            if (cfg != null && !cfg.getId().equals(DBcfg.getId())) {
                throw new RuntimeException("命名空间为  " + namespace + ", 类型为 " + type + ", 键为 " + key + " 的配置已存在 ");
            }
        } else if (config.getNamespace() == null && config.getType() == null) {
            key = config.getConfigKey().trim();
            namespace = DBcfg.getNamespace().trim();
            type = DBcfg.getType().trim();
            cfg = this.configDao.queryConfigByKey(namespace, type, key);
            if (cfg != null && !cfg.getId().equals(DBcfg.getId())) {
                throw new RuntimeException("命名空间为  " + namespace + ", 类型为 " + type + ", 键为 " + key + " 的配置已存在 ");
            }
        }

        return true;
    }

    private void updateVersion(Config config) {
        String type = config.getType();
        ConfigVersion cv = this.configVersionDao.queryByType(type);
        if (cv == null) {
            cv = new ConfigVersion();
            cv.setType(type);
            cv.setVersion(Integer.valueOf(1));
            this.configVersionDao.add(cv);
        } else {
            this.configVersionDao.update(cv);
        }

    }

    @Transactional(
        rollbackFor = {Exception.class}
    )
    public void deleteConfig(Long id) {
        CheckUtils.valueIsNull(id, "id");
        Config config = (Config)this.configDao.get(id);
        if (config != null) {
            if (!config.getDeletable().booleanValue()) {
                throw new RuntimeException("配置不可以删除");
            }

            this.configDao.delete(id);
            List<Config> configs = this.configDao.queryConfigByTypes(config.getNamespace(), new String[]{config.getType()});
            if (configs != null && configs.size() != 0) {
                this.updateVersion(config);
            } else {
                String type = config.getType();
                ConfigVersion configVersion = this.configVersionDao.queryByType(type);
                this.configVersionDao.delete(configVersion.getId());
            }
        }

    }

    public Map<String, Map<String, Object>> loadConfig(String namespace, String... types) {
        namespace = namespace.trim();
        List<Config> configs = null;
        if (types != null && types.length > 0) {
            for(int i = 0; i < types.length; ++i) {
                types[i] = types[i].trim();
            }

            configs = this.configDao.queryConfigByTypes(namespace, types);
        } else {
            configs = this.configDao.queryConfigByNamespace(namespace);
        }

        Map<String, Map<String, Object>> cfgs = null;
        if (configs != null && configs.size() > 0) {
            cfgs = new ConcurrentHashMap();
            Iterator var5 = configs.iterator();

            while(var5.hasNext()) {
                Config config = (Config)var5.next();
                String type = config.getType();
                Map<String, Object> datas = (Map)cfgs.get(type);
                if (datas == null) {
                    datas = new ConcurrentHashMap();
                    cfgs.put(type, datas);
                }

                try {
                    ((Map)datas).put(config.getConfigKey(), ConfigUtil.XmlStrToObject(config.getValueType(), config.getValueDataType(), config.getValue()));
                } catch (DocumentException var10) {
                    logger.error("加载数据出错：" + var10.getMessage() + "      错误数据是:" + config.getValue());
                }
            }
        }

        return cfgs;
    }

    public Map<String, Integer> queryConfigVersion(String namespace, String... types) {
        List<ConfigVersion> versions = null;
        if (types != null && types.length > 0) {
            versions = this.configVersionDao.queryByTypes(types);
        } else {
            versions = this.configVersionDao.queryAll();
        }

        Map<String, Integer> configVersions = new ConcurrentHashMap();
        if (versions != null && versions.size() > 0) {
            Iterator var5 = versions.iterator();

            while(var5.hasNext()) {
                ConfigVersion version = (ConfigVersion)var5.next();
                configVersions.put(version.getType(), version.getVersion());
            }
        }

        return configVersions;
    }

    public List<Config> queryConfig(Config config) {
        if (config.getNamespace() != null && !config.getNamespace().trim().equals("")) {
            config.setNamespace("%" + config.getNamespace().trim() + "%");
        } else {
            config.setNamespace((String)null);
        }

        if (config.getType() != null && !config.getType().trim().equals("")) {
            config.setType("%" + config.getType().trim() + "%");
        } else {
            config.setType((String)null);
        }

        if (config.getConfigKey() != null && !config.getConfigKey().trim().equals("")) {
            config.setConfigKey("%" + config.getConfigKey().trim() + "%");
        } else {
            config.setConfigKey((String)null);
        }

        return this.configDao.queryConfig(config);
    }

    public Config queryConfigById(Long id) {
        CheckUtils.valueIsNull(id, "id");
        return (Config)this.configDao.get(id);
    }

    public Boolean configIsOnly(Config config) {
        CheckUtils.valueIsNull(config, "namespace,type,configKey");
        String key = config.getConfigKey().trim();
        String namespace = config.getNamespace().trim();
        String type = config.getType().trim();
        Config cfg = this.configDao.queryConfigByKey(namespace, type, key);
        return cfg == null;
    }

    public void setConfigDao(ConfigDao configDao) {
        this.configDao = configDao;
    }

    public void setConfigVersionDao(ConfigVersionDao configVersionDao) {
        this.configVersionDao = configVersionDao;
    }

    public ConfigNamespace queryConfigNamespace(String namespace) {
        return this.configNamespaceDao.queryByCode(namespace);
    }

    public ConfigType queryConfigType(String type) {
        return this.configTypeDao.queryByCode(type);
    }

    public void setConfigNamespaceDao(ConfigNamespaceDao configNamespaceDao) {
        this.configNamespaceDao = configNamespaceDao;
    }

    public void setConfigTypeDao(ConfigTypeDao configTypeDao) {
        this.configTypeDao = configTypeDao;
    }

    public void saveOrUpdateConfigNamespace(ConfigNamespace configNamespace) {
        if (configNamespace.getId() != null) {
            this.configNamespaceDao.update(configNamespace);
        } else {
            this.configNamespaceDao.add(configNamespace);
        }

    }

    public void saveOrUpdateConfigType(ConfigType configType) {
        if (configType.getId() != null) {
            this.configTypeDao.update(configType);
        } else {
            this.configTypeDao.add(configType);
        }

    }

    public List<Config> queryConfigByNamespace(String namespace) {
        return this.configDao.queryConfigByNamespace(namespace);
    }

    public List<Config> queryConfigByType(String type) {
        return this.configDao.queryConfigByType(type);
    }

    public void deleteConfigNamespace(String namespace) {
        this.configNamespaceDao.delete(namespace);
    }

    public void deleteConfigType(String type) {
        this.configTypeDao.delete(type);
    }

    public List<ConfigNamespace> queryConfigNamespace() {
        return this.configNamespaceDao.getAll();
    }

    public List<ConfigType> queryConfigType() {
        return this.configTypeDao.getAll();
    }
}

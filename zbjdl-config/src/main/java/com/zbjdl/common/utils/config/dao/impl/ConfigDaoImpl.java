package com.zbjdl.common.utils.config.dao.impl;

import com.zbjdl.common.persistence.mybatis.GenericDaoDefault;
import com.zbjdl.common.utils.config.dao.ConfigDao;
import com.zbjdl.common.utils.config.entity.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigDaoImpl extends GenericDaoDefault<Config> implements ConfigDao {
    public ConfigDaoImpl() {
    }

    public List<Config> queryConfig(Config config) {
        return this.query("queryConfig", new Object[]{config});
    }

    public Config queryConfigByKey(String namespace, String type, String key) {
        Map<String, Object> params = new HashMap();
        params.put("configKey", key);
        params.put("namespace", namespace);
        params.put("type", type);
        List<Config> configs = this.query("queryConfigByKey", new Object[]{params});
        return configs != null && configs.size() > 0 ? (Config)configs.get(0) : null;
    }

    public List<Config> queryConfigByNamespace(String namepace) {
        return this.query("queryConfigByNamespace", new Object[]{namepace});
    }

    public List<Config> queryConfigByTypes(String namespace, String... types) {
        Map<String, Object> params = new HashMap();
        params.put("namespace", namespace);
        if (types != null && types.length != 0) {
            params.put("types", types);
        } else {
            params.put("types", (Object)null);
        }

        return this.query("queryConfigByTypes", new Object[]{params});
    }

    public List<Config> queryConfigByType(String type) {
        return this.query("queryConfigByType", new Object[]{type});
    }
}

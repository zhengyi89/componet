package com.zbjdl.common.utils.config.dao.impl;

import com.zbjdl.common.persistence.mybatis.GenericDaoDefault;
import com.zbjdl.common.utils.config.dao.ConfigVersionDao;
import com.zbjdl.common.utils.config.entity.ConfigVersion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigVersionDaoImpl extends GenericDaoDefault<ConfigVersion> implements ConfigVersionDao {
    public ConfigVersionDaoImpl() {
    }

    public List<ConfigVersion> queryByTypes(String[] types) {
        Map<String, String[]> params = new HashMap();
        params.put("types", types);
        return this.query("queryByTypes", new Object[]{params});
    }

    public ConfigVersion queryByType(String type) {
        List<ConfigVersion> vs = this.query("queryByType", new Object[]{type});
        return vs != null && vs.size() > 0 ? (ConfigVersion)vs.get(0) : null;
    }

    public List<ConfigVersion> queryAll() {
        return this.query("getAll", new Object[0]);
    }
}

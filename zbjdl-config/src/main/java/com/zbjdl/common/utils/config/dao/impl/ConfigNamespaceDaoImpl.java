package com.zbjdl.common.utils.config.dao.impl;

import com.zbjdl.common.persistence.mybatis.GenericDaoDefault;
import com.zbjdl.common.utils.config.dao.ConfigNamespaceDao;
import com.zbjdl.common.utils.config.entity.ConfigNamespace;

public class ConfigNamespaceDaoImpl extends GenericDaoDefault<ConfigNamespace> implements ConfigNamespaceDao {
    public ConfigNamespaceDaoImpl() {
    }

    public ConfigNamespace queryByCode(String code) {
        return (ConfigNamespace)super.queryOne("queryByCode", new Object[]{code});
    }
}

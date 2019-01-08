package com.zbjdl.common.utils.config.dao.impl;

import com.zbjdl.common.persistence.mybatis.GenericDaoDefault;
import com.zbjdl.common.utils.config.dao.ConfigTypeDao;
import com.zbjdl.common.utils.config.entity.ConfigType;

public class ConfigTypeDaoImpl extends GenericDaoDefault<ConfigType> implements ConfigTypeDao {
    public ConfigTypeDaoImpl() {
    }

    public ConfigType queryByCode(String code) {
        return (ConfigType)this.queryOne("queryByCode", new Object[]{code});
    }
}

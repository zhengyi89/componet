package com.zbjdl.common.respository.mybatis.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * title: <br/>
 * description: 描述<br/>
 * Copyright: Copyright (c)2014<br/>
 * Company: 云宝金服<br/>
 *
 */
public abstract class BaseOptimisticLockingInterceptor implements Interceptor, Serializable {

    private static final long serialVersionUID = 4596430444388728543L;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseOptimisticLockingInterceptor.class);

    protected static ConcurrentMap<String, Boolean> assignableFromVersionMap = new ConcurrentHashMap<String, Boolean>();

    /**
     * 拦截的ID，在mapper中的id，可以匹配正则
     */
    protected static String SQL_PATTERN;

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        SQL_PATTERN = properties.getProperty("sqlPattern");
        if (StringUtils.isBlank(SQL_PATTERN)) {
            SQL_PATTERN = ".*.update.*";
        }
    }

}

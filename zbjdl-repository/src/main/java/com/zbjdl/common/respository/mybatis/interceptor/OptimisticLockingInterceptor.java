package com.zbjdl.common.respository.mybatis.interceptor;

import com.zbjdl.common.respository.OptimisticLockingException;
import com.zbjdl.common.respository.entity.VersionableEntity;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

/**
 * title: 乐观锁异常拦截器<br/>
 * description: 适用于直接或间接继承自 VersionableEntity 的 Entity<br/>
 * Copyright: Copyright (c)2014<br/>
 * Company: 云宝金服<br/>
 *
 */
@Intercepts({@Signature(type = Executor.class, method = "update",
        args = {MappedStatement.class, Object.class})})
public class OptimisticLockingInterceptor extends BaseOptimisticLockingInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (mappedStatement.getId().matches(SQL_PATTERN)) {
            Object result = invocation.proceed();
            if (isAssignableFromVersion(mappedStatement.getParameterMap().getType())) {
                if (result instanceof Integer && (Integer) result == 0) {
                    throw new OptimisticLockingException("乐观锁异常");
                }
            }
            return result;
        } else {
            return invocation.proceed();
        }
    }

    private boolean isAssignableFromVersion(Class<?> type) {
        if (null == type) {
            return false;
        }

        String key = type.getName();
        Boolean valid = assignableFromVersionMap.get(key);
        if (null == valid) {
            valid = type == VersionableEntity.class || isAssignableFromVersion(type.getSuperclass());
            assignableFromVersionMap.putIfAbsent(key, valid);
        }
        return valid;
    }

}

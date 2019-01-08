//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.config;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

public class CacheInterceptor implements MethodInterceptor {
    private static Logger logger = LoggerFactory.getLogger(CacheInterceptor.class);

    public CacheInterceptor() {
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object src = invocation.getThis();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();
        Class<?> clazz = this.getTargetClass(src);
        CacheConfig cacheConfig = CacheAOPUtils.getCacheConfig(clazz, method, args);
        logger.debug("invoke target[" + clazz.getSimpleName() + "." + method.getName() + "] cacheConfig[" + (cacheConfig != null) + "]");
        if (cacheConfig == null) {
            return invocation.proceed();
        } else {
            String cacheKey = CacheAOPUtils.getCacheKey(cacheConfig, args);
            if (cacheConfig.isUpdate()) {
                Object var22;
                try {
                    var22 = invocation.proceed();
                } finally {
                    CacheAOPUtils.removeCachedResult(cacheConfig, cacheKey);
                }

                return var22;
            } else {
                CachedResult cahcedResult = CacheAOPUtils.getCachedResult(cacheConfig, cacheKey);
                if (cahcedResult != null) {
                    logger.debug("return cached result for [" + clazz.getSimpleName() + "." + method.getName() + "] , obj[" + cahcedResult.getResult() + "] exception[" + cahcedResult.getException() + "]");
                }

                if (cahcedResult == null) {
                    Throwable exception = null;
                    Object result = null;

                    Object var11;
                    try {
                        result = invocation.proceed();
                        var11 = result;
                    } catch (Throwable var20) {
                        exception = var20;
                        throw var20;
                    } finally {
                        if (result != null) {
                            CacheAOPUtils.putCachedResult(cacheConfig, cacheKey, result, exception);
                        }

                    }

                    return var11;
                } else {
                    return cahcedResult.result();
                }
            }
        }
    }

    private Class<?> getTargetClass(Object bean) {
        try {
            return AopUtils.isJdkDynamicProxy(bean) ? ((Advised)bean).getTargetSource().getTarget().getClass() : bean.getClass();
        } catch (Throwable var3) {
            return bean.getClass();
        }
    }
}

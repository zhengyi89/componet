//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.config;

import com.zbjdl.common.quata.Executable;
import com.zbjdl.common.utils.BeanUtils;
import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.cache.local.LocalCacheUtils;
import com.zbjdl.common.utils.cache.local.param.LocalParam;
import com.zbjdl.common.utils.cache.remote.RemoteCacheUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheAOPUtils {
    private static Logger logger = LoggerFactory.getLogger(CacheAOPUtils.class);
    private static Map<String, CacheConfig> configs = new HashMap();

    public CacheAOPUtils() {
    }

    public static CacheConfig getConfig(String key, Method method) {
        Cache cache = (Cache)method.getAnnotation(Cache.class);
        if (cache != null) {
            CacheConfig config = (CacheConfig)configs.get(key);
            if (config != null) {
                return config;
            } else {
                String name = cache.name() != null && cache.name().trim().length() != 0 ? cache.name() : key + "";
                List<CacheKeyConfig> cacheKeys = getCacheKeyIndex(method.getParameterAnnotations());
                boolean isUpdate = "UPDATE".equalsIgnoreCase(cache.usage());
                config = new CacheConfig(name, cache.cacheRegion(), cacheKeys, isUpdate, cache.type(), cache.remote().time(), cache.local().eternal(), cache.local().timeToIdleSeconds(), cache.local().timeToLiveSeconds());
                configs.put(key, config);
                return config;
            }
        } else {
            return null;
        }
    }

    private static List<CacheKeyConfig> getCacheKeyIndex(Annotation[][] anns) {
        if (anns != null && anns.length != 0) {
            List<CacheKeyConfig> cacheKeys = null;

            for(int i = 0; i < anns.length; ++i) {
                Annotation[] ans = anns[i];
                Annotation[] arr$ = ans;
                int len$ = ans.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Annotation an = arr$[i$];
                    if (an.annotationType().equals(CacheKey.class)) {
                        if (cacheKeys == null) {
                            cacheKeys = new ArrayList();
                        }

                        CacheKeyConfig config = new CacheKeyConfig();
                        config.setIndex(i);
                        config.setProperty(((CacheKey)an).property());
                        cacheKeys.add(config);
                    }
                }
            }

            return cacheKeys;
        } else {
            return null;
        }
    }

    public static CacheConfig getCacheConfig(Class<?> clz, Method method, Object[] arguments) {
        Method realMethod = method;
        if (method.getAnnotation(Cache.class) == null && method.getDeclaringClass().isInterface()) {
            try {
                realMethod = clz.getDeclaredMethod(method.getName(), method.getParameterTypes());
            } catch (Exception var7) {
                ;
            }
        }

        Cache cache = (Cache)method.getAnnotation(Cache.class);
        if (cache != null) {
            String configKey = buildCacheConfigKey(clz, realMethod);
            CacheConfig config = getConfig(configKey, realMethod);
            return config;
        } else {
            return null;
        }
    }

    private static String buildCacheConfigKey(Class<?> clz, Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(clz.getName().hashCode());
        builder.append("_");
        builder.append(method.getName().hashCode());
        Class<?>[] types = method.getParameterTypes();
        if (types != null && types.length > 0) {
            builder.append("_");
            StringBuilder sb = new StringBuilder();
            Class[] arr$ = types;
            int len$ = types.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Class<?> type = arr$[i$];
                sb.append(type.getName());
            }

            builder.append(sb.toString().hashCode());
        }

        return builder.toString();
    }

    public static String getCacheKey(CacheConfig config, Object[] args) {
        String cacheName = config.getName();
        List<CacheKeyConfig> cacheKeyConfigs = config.getCacheKeyConfigs();
        StringBuffer sb = new StringBuffer();
        sb.append(cacheName);
        Object[] keys = null;
        if (cacheKeyConfigs != null && cacheKeyConfigs.size() != args.length) {
            keys = new Object[cacheKeyConfigs.size()];

            for(int i = 0; i < keys.length; ++i) {
                CacheKeyConfig keyConfig = (CacheKeyConfig)cacheKeyConfigs.get(i);
                Object obj = args[keyConfig.getIndex()];
                if (!CheckUtils.isEmpty(keyConfig.getProperty())) {
                    obj = BeanUtils.getProperty(obj, keyConfig.getProperty());
                }

                keys[i] = obj;
            }
        } else {
            keys = args;
        }

        return LocalCacheUtils.generateCacheKey(cacheName, keys);
    }

    public static CachedResult getCachedResult(CacheConfig cacheConfig, String cacheKey) {
        switch(cacheConfig.getType() == null ? CacheTypeEnum.LOCAL : cacheConfig.getType()) {
        case LOCAL:
            if (LocalCacheUtils.contains(cacheConfig.getCacheRegion(), cacheKey)) {
                return (CachedResult)LocalCacheUtils.get(cacheConfig.getCacheRegion(), cacheKey);
            }
        case THREAD:
            return null;
        case REMOTE:
            return (CachedResult)RemoteCacheUtils.get(cacheKey);
        default:
            return null;
        }
    }

    public static void putCachedResult(CacheConfig cacheConfig, String cacheKey, Object result, Throwable exception) {
        switch(cacheConfig.getType() == null ? CacheTypeEnum.LOCAL : cacheConfig.getType()) {
        case LOCAL:
            if (cacheConfig.getTimeToIdleSeconds() != -1 && cacheConfig.getTimeToLiveSeconds() != -1) {
                LocalCacheUtils.put(cacheConfig.getCacheRegion(), cacheKey, new CachedResult(result, exception), new LocalParam(cacheConfig.isEternal(), cacheConfig.getTimeToIdleSeconds(), cacheConfig.getTimeToLiveSeconds()));
                return;
            }

            LocalCacheUtils.put(cacheConfig.getCacheRegion(), cacheKey, new CachedResult(result, exception));
            break;
        case REMOTE:
            if (cacheConfig.getTime() != -1) {
                RemoteCacheUtils.put(cacheKey, new CachedResult(result, exception), cacheConfig.getTime());
                return;
            }

            RemoteCacheUtils.put(cacheKey, new CachedResult(result, exception));
        case THREAD:
        }

    }

    public static void removeCachedResult(CacheConfig cacheConfig, String cacheKey) {
        switch(cacheConfig.getType() == null ? CacheTypeEnum.LOCAL : cacheConfig.getType()) {
        case LOCAL:
            LocalCacheUtils.remove(cacheConfig.getCacheRegion(), cacheKey);
            LocalCacheUtils.notify(cacheConfig.getCacheRegion(), cacheKey);
            break;
        case REMOTE:
            RemoteCacheUtils.remove(cacheKey);
        case THREAD:
        }

    }

    public static Object getResult(Executable execute, CacheConfig cacheConfig, String cacheKey, String message) throws Throwable {
        CachedResult cahcedResult = getCachedResult(cacheConfig, cacheKey);
        if (cahcedResult != null) {
            logger.debug("return cached result for [" + message + "] , obj[" + cahcedResult.getResult() + "] exception[" + cahcedResult.getException() + "]");
        }

        if (cahcedResult == null) {
            CachedResult cr = (CachedResult)execute.execute();
            Object result = cr.result();
            putCachedResult(cacheConfig, cacheKey, result, cr.getException());
            return result;
        } else {
            return cahcedResult.result();
        }
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.invoker;

import com.zbjdl.common.utils.ThreadContextUtils;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringInvokerUtils implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(SpringInvokerUtils.class);
    private static Map<ClassLoader, ApplicationContext> contexts = new HashMap();
    private static Map<String, ApplicationContext> namedContexts = new HashMap();

    public SpringInvokerUtils() {
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        contexts.put(Thread.currentThread().getContextClassLoader(), context);
        if (context.getClassLoader() != Thread.currentThread().getContextClassLoader()) {
            contexts.put(context.getClassLoader(), context);
        }

        namedContexts.put(getContextName(), context);
    }

    public static void setDefaultContext(ApplicationContext context) {
        contexts.put(Thread.currentThread().getContextClassLoader(), context);
        if (context.getClassLoader() != Thread.currentThread().getContextClassLoader()) {
            contexts.put(context.getClassLoader(), context);
        }

        namedContexts.put(getContextName(), context);
    }

    public static boolean hasInit() {
        ApplicationContext context = getApplicationContext();
        return null != context;
    }

    public static boolean hasLocalService(String servicePath) {
        return hasLocalService(servicePath, (String)null);
    }

    public static boolean hasLocalService(String servicePath, String appName) {
        ApplicationContext context = getApplicationContext(appName);
        if (context == null) {
            logger.warn("ApplicationContext not inited. classLoader : " + Thread.currentThread().getContextClassLoader().toString());
            return false;
        } else {
            return context.containsBean(servicePath);
        }
    }

    public static Object getBean(String servicePath) {
        ApplicationContext context = getApplicationContext();
        if (context == null) {
            logger.warn("ApplicationContext not inited. classLoader : " + Thread.currentThread().getContextClassLoader().toString());
            return null;
        } else {
            return context.getBean(servicePath);
        }
    }

    public static Object invoker(String servicePath, ApplicationContext context, String actionName, Object[] parameters) throws Exception {
        Object provider = context.getBean(servicePath);
        if (null == provider) {
            throw new EventRuntimeException(99001002, "");
        } else {
            return ClassInvokerUtils.invoker(servicePath, provider, actionName, parameters);
        }
    }

    public static Object invoker(String servicePath, String actionName, Object[] parameters) throws Exception {
        ApplicationContext context = getApplicationContext();
        return invoker(servicePath, context, actionName, parameters);
    }

    public static String getContextName() {
        ApplicationContext context = getApplicationContext();
        if (context == null) {
            return null;
        } else {
            String contextName = context.getId();
            if (contextName != null && contextName.contains("/")) {
                contextName = contextName.substring(contextName.lastIndexOf("/") + 1);
            } else {
                contextName = context.getDisplayName();
                if (contextName != null) {
                    contextName = contextName.replace(" ", "-");
                }
            }

            return contextName;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return getApplicationContext((String)null);
    }

    public static ApplicationContext getApplicationContext(String appName) {
        ApplicationContext context = null;
        if (StringUtils.isBlank(appName) && ThreadContextUtils.contextInitialized()) {
            appName = ThreadContextUtils.getContext().getAppName();
        }

        if (StringUtils.isNotBlank(appName)) {
            context = (ApplicationContext)namedContexts.get(appName);
        }

        if (context == null) {
            context = (ApplicationContext)contexts.get(Thread.currentThread().getContextClassLoader());
        }

        return context;
    }
}

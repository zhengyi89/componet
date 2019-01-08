package com.zbjdl.common.utils.config;

import com.zbjdl.common.utils.CommonUtils;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConfigurationUtils {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationUtils.class);
    public static String CONFIG_TYPE_TEXT_RESOURCES = "config_type_text_resources";
    public static String CONFIG_TYPE_APP = "config_type_app";
    public static String CONFIG_TYPE_SYS = "config_type_sys";
    private static boolean disableconfig = false;
    private static boolean mockconfig = false;
    private static ConfigRepository configRepository;
    private static ApplicationContext context;

    public ConfigurationUtils() {
    }

    private static void checkInitialized() {
        if (!initialized()) {
            throw new RuntimeException("config utils not initialized!");
        }
    }

    public static synchronized void destory() {
        if (!initialized() || disableconfig || mockconfig) {
            ;
        }
    }

    private static void loadApplicationContext() {
        try {
            context = new ClassPathXmlApplicationContext("config-spring/config-service.spring.xml");
        } catch (BeansException var1) {
            logger.error(var1.getMessage());
            var1.printStackTrace();
        }

    }

    private static void loadSpringBootApplicationContext() {
        try {
            context = new ClassPathXmlApplicationContext("config-spring/configForBootApplicationContext.xml");
        } catch (BeansException var1) {
            logger.error(var1.getMessage());
            var1.printStackTrace();
        }

    }

    public static boolean initialized() {
        return configRepository != null;
    }

    public static synchronized void init() {
        if (!initialized()) {
            loadApplicationContext();
            logger.info("init ConfigurationUtils, using DefaultConfigRepository");
            configRepository = (ConfigRepository)context.getBean("defaultConfigRepository");
        }
    }

    public static synchronized void initForBoot() {
        if (!initialized()) {
            loadSpringBootApplicationContext();
            logger.info("init ConfigurationUtils, using DefaultConfigRepository");
            configRepository = (ConfigRepository)context.getBean("defaultConfigRepository");
        }
    }

    public static void loadConfig(String... configTypes) {
        checkInitialized();
        configRepository.loadConfig(configTypes);
    }

    public static void loadAllConfig() {
        checkInitialized();
        configRepository.loadAllConfig();
    }

    public static ConfigParam getConfigParam(String configType, String configKey) {
        checkInitialized();
        return configRepository.getConfig(configType, configKey);
    }

    public static <T> ConfigParam<T> getConfigParam(String configType, String configKey, Class<T> type) {
        checkInitialized();
        return configRepository.getConfig(configType, configKey);
    }

    public static <T> ConfigParam<Map<String, T>> getMapConfigParam(String configType, String configKey, Class<T> type) {
        checkInitialized();
        return configRepository.getConfig(configType, configKey);
    }

    public static <T> ConfigParam<List<T>> getListConfigParam(String configType, String configKey, Class<T> type) {
        checkInitialized();
        return configRepository.getConfig(configType, configKey);
    }

    public static ConfigParamGroup getConfigParamGroup(String configType) {
        checkInitialized();
        return configRepository.getConfigGroup(configType);
    }

    public static ConfigParam getAppConfigParam(String configKey) {
        checkInitialized();
        return getConfigParam(CONFIG_TYPE_APP, configKey);
    }

    public static ConfigParam getSysConfigParam(String configKey) {
        checkInitialized();
        return getConfigParam(CONFIG_TYPE_SYS, configKey);
    }

    public static void addListener(ConfigNotifyListener listener) {
        configRepository.addListener(listener);
    }

    public static ApplicationContext getContext() {
        return context;
    }

    static {
        String disableconfig_prop = System.getProperty("disableconfig");
        if ("true".equals(disableconfig_prop)) {
            disableconfig = true;
            configRepository = (ConfigRepository)CommonUtils.newInstance("com.zbjdl.common.utils.config.mock.NoneConfigRepository");
            logger.info("disableconfig=true, using NoneConfigRepository");
        }else {
			synchronized (ConfigurationUtils.class) {
				if (configRepository==null) {
					init();
				}
			}
		}

        if (!disableconfig && !mockconfig) {
            logger.info("load spring config");
        }

    }
}

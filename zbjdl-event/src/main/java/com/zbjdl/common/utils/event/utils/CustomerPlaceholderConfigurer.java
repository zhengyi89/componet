//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class CustomerPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(CustomerPlaceholderConfigurer.class);
    private static ThreadLocal<CustomerPlaceholderConfigurer> context = new ThreadLocal();
    private Properties systemProperties;
    private Properties userProperties;

    public CustomerPlaceholderConfigurer() {
    }

    public void afterPropertiesSet() {
        this.systemProperties = new PropertiesEx();
        String fileName = System.getProperty("place.holder.file.name");
        if (StringUtils.isNotBlank(fileName)) {
            File file = new File(fileName);
            if (file.exists()) {
                try {
                    FileInputStream in = new FileInputStream(file);

                    try {
                        this.systemProperties.load(in);
                        SystemConfigUtils.put(this.systemProperties);
                    } finally {
                        if (null != in) {
                            in.close();
                        }

                    }
                } catch (Exception var8) {
                    logger.error("fileName : " + fileName, var8);
                }
            }
        }

        context.set(this);
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            Properties mergedProps = this.mergeProperties();
            this.convertProperties(mergedProps);
            this.userProperties = mergedProps;
            SystemConfigUtils.put(this.userProperties);
            this.processProperties(beanFactory, mergedProps);
        } catch (IOException var3) {
            logger.info("Could not load properties : " + var3.getMessage());
        }

    }

    public static CustomerPlaceholderConfigurer getCurrentThreadCustomerPlaceholderConfigurer() {
        return (CustomerPlaceholderConfigurer)context.get();
    }

    protected String resolvePlaceholder(String placeholder, Properties props) {
        return this.systemProperties.containsKey(placeholder) ? this.systemProperties.getProperty(placeholder) : props.getProperty(placeholder);
    }

    public String getProperty(String placeholder) {
        return this.systemProperties.containsKey(placeholder) ? this.systemProperties.getProperty(placeholder) : this.userProperties.getProperty(placeholder);
    }
}

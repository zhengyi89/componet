//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class SystemConfigUtils {
    private static Map<String, List<SystemConfigUtils.PropertyValue>> cache = Collections.synchronizedMap(new HashMap());

    public SystemConfigUtils() {
    }

    public static void put(Properties properties) {
        Iterator<Object> iter = properties.keySet().iterator();
        Object key = null;
        Object value = null;

        while(iter.hasNext()) {
            key = iter.next();
            if (key != null) {
                value = properties.get(key);
                put(key.toString(), new SystemConfigUtils.PropertyValue(value == null ? null : value.toString()));
            }
        }

    }

    public static void putFromDB(Map<String, List<SystemConfigUtils.PropertyValue>> properties) {
        Iterator<String> iter = properties.keySet().iterator();
        String key = null;
        List value = null;

        while(iter.hasNext()) {
            key = (String)iter.next();
            value = (List)properties.get(key);
            cache.put(key, value);
        }

    }

    public static void put(String name, SystemConfigUtils.PropertyValue propertyValue) {
        if (cache.containsKey(name)) {
            List<SystemConfigUtils.PropertyValue> pvs = (List)cache.get(name);
            Iterator var3 = pvs.iterator();

            while(var3.hasNext()) {
                SystemConfigUtils.PropertyValue pv = (SystemConfigUtils.PropertyValue)var3.next();
                if (StringUtils.equals(pv.value, propertyValue.value)) {
                    pv.fromDt = propertyValue.fromDt;
                    pv.thruDt = propertyValue.thruDt;
                    return;
                }
            }

            pvs.add(propertyValue);
        } else {
            List<SystemConfigUtils.PropertyValue> pvs = new ArrayList();
            pvs.add(propertyValue);
            cache.put(name, pvs);
        }

    }

    public static void put(String name, String value) {
        if (StringUtils.isNotBlank(name)) {
            put(name, new SystemConfigUtils.PropertyValue(value));
        }

    }

    public void init() {
        put(System.getProperties());
    }

    public static List<SystemConfigUtils.PropertyValue> getPropertyList(String key) {
        return (List)cache.get(key);
    }

    public static String getProperty(String key) {
        List<SystemConfigUtils.PropertyValue> pvs = (List)cache.get(key);
        if (null == pvs) {
            return null;
        } else {
            Iterator var2 = pvs.iterator();

            SystemConfigUtils.PropertyValue pv;
            do {
                if (!var2.hasNext()) {
                    return null;
                }

                pv = (SystemConfigUtils.PropertyValue)var2.next();
            } while(pv.fromDt >= System.currentTimeMillis() || pv.thruDt <= System.currentTimeMillis());

            return pv.value;
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return cache.containsKey(key) ? getProperty(key) : defaultValue;
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.valueOf(getProperty(key)).booleanValue();
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        return cache.containsKey(key) ? BooleanUtils.toBoolean(getProperty(key)) : defaultValue;
    }

    public static int getIntProperty(String key) {
        return NumberUtils.toInt(getProperty(key));
    }

    public static int getIntProperty(String key, int defaultValue) {
        return NumberUtils.toInt(getProperty(key), defaultValue);
    }

    public static long getLongProperty(String key) {
        return NumberUtils.toLong(getProperty(key));
    }

    public static long getLongProperty(String key, long defaultValue) {
        return NumberUtils.toLong(getProperty(key), defaultValue);
    }

    public static int getAppGroupID() {
        int id = getIntProperty("app.group");
        if (id < 1) {
            id = 1;
        }

        return id;
    }

    public static boolean isDebug() {
        return getBooleanProperty("is.debug", false);
    }

    public static boolean isCmsOpen() {
        return getBooleanProperty("db.cms.open", false);
    }

    public static class PropertyValue {
        public String value;
        public long fromDt;
        public long thruDt;

        public PropertyValue(String value) {
            this.value = value;
            this.fromDt = 0L;
            this.thruDt = 9223372036854775807L;
        }

        public PropertyValue(String value, String fromDt, String thruDt) {
            this.value = value;
            this.fromDt = NumberUtils.toLong(fromDt, -9223372036854775808L);
            this.thruDt = NumberUtils.toLong(thruDt, 9223372036854775807L);
        }

        public PropertyValue(String value, long fromDt, long thruDt) {
            this.value = value;
            this.fromDt = fromDt;
            this.thruDt = thruDt;
        }
    }
}

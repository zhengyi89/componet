package com.zbjdl.common.utils.config;

import com.zbjdl.common.utils.CheckUtils;
import java.util.LinkedHashMap;
import java.util.Map;

public class TextResourceUtils {
    public static String CONFIG_TYPE_TEXT_RESOURCES = "config_type_text_resources";
    public static String CONFIG_TYPE_EXCEPTION_MESSAGES = "config_type_exception_messages";
    public static String CONFIG_TYPE_EXCEPTION_CODES = "config_type_exception_codes";
    public static String CONFIG_TYP_SYS = "config_type_sys";
    public static String CONFIG_TYPE_APP = "config_type_app";
    private static ConfigParamGroup<Map> textResource;
    private static ConfigParamGroup<Map> exceptionMessages;
    private static ConfigParamGroup exceptionCodes;
    private static ConfigParamGroup sysGroup;
    private static ConfigParamGroup appGroup;

    public TextResourceUtils() {
    }

    public static String getText(String textType, String textKey) {
        CheckUtils.notEmpty(textType, "textType");
        CheckUtils.notEmpty(textType, "textKey");
        ConfigParam<Map> map = textResource.getConfig(textType);
        return map != null && !CheckUtils.isEmpty(map.getValue()) ? (String)((Map)map.getValue()).get(textKey) : null;
    }

    public static String getTextByConfigType(String configType, String textType, String textKey) {
        CheckUtils.notEmpty(configType, "configType");
        CheckUtils.notEmpty(textType, "textType");
        CheckUtils.notEmpty(textType, "textKey");
        ConfigParam<Map> map = null;
        if (ConfigurationUtils.CONFIG_TYPE_TEXT_RESOURCES.equals(configType)) {
            map = textResource.getConfig(textType);
        } else if (ConfigurationUtils.CONFIG_TYPE_APP.equals(configType)) {
            map = appGroup.getConfig(textType);
        } else if (ConfigurationUtils.CONFIG_TYPE_SYS.equals(configType)) {
            map = sysGroup.getConfig(textType);
        }

        return map != null && !CheckUtils.isEmpty(map.getValue()) ? (String)((Map)map.getValue()).get(textKey) : null;
    }

    public static Map getTextMapByConfigType(String configType, String textType) {
        CheckUtils.notEmpty(configType, "configType");
        CheckUtils.notEmpty(textType, "textType");
        ConfigParam<Map> map = null;
        if (ConfigurationUtils.CONFIG_TYPE_TEXT_RESOURCES.equals(configType)) {
            map = textResource.getConfig(textType);
        } else if (ConfigurationUtils.CONFIG_TYPE_APP.equals(configType)) {
            map = appGroup.getConfig(textType);
        } else if (ConfigurationUtils.CONFIG_TYPE_SYS.equals(configType)) {
            map = sysGroup.getConfig(textType);
        }

        return map == null ? null : new LinkedHashMap((Map)map.getValue());
    }

    public static Map getTextMap(String textType) {
        CheckUtils.notEmpty(textType, "textType");
        ConfigParam<Map> map = textResource.getConfig(textType);
        return map == null ? null : new LinkedHashMap((Map)map.getValue());
    }

    public static String getExceptionMessage(String code) {
        CheckUtils.notEmpty(code, "code");
        if (code.length() != 12) {
            throw new RuntimeException("invalid exception code : " + code);
        } else {
            String baseCode = code.substring(0, 9);
            String subCode = code.substring(9, 12);
            return null;
        }
    }

    public static String getExceptionMessage(String exceptionClass, String code) {
        CheckUtils.notEmpty(exceptionClass, "exceptionClass");
        CheckUtils.notEmpty(code, "code");
        exceptionClass = exceptionClass.trim();
        code = code.trim();
        ConfigParam<Map> map = exceptionMessages.getConfig(exceptionClass);
        return map != null && map.getValue() != null ? (String)((Map)map.getValue()).get(code) : null;
    }

    public static String getExceptionCode(String exceptionClass, String code) {
        CheckUtils.notEmpty(exceptionClass, "exceptionClass");
        exceptionClass = exceptionClass.trim();
        code = code.trim();
        ConfigParam<String> value = exceptionCodes.getConfig(exceptionClass);
        return value == null ? null : (String)value.getValue() + code;
    }

    static {
        textResource = ConfigurationUtils.getConfigParamGroup(CONFIG_TYPE_TEXT_RESOURCES);
        exceptionMessages = ConfigurationUtils.getConfigParamGroup(CONFIG_TYPE_EXCEPTION_MESSAGES);
        exceptionCodes = ConfigurationUtils.getConfigParamGroup(CONFIG_TYPE_EXCEPTION_CODES);
        sysGroup = ConfigurationUtils.getConfigParamGroup(CONFIG_TYP_SYS);
        appGroup = ConfigurationUtils.getConfigParamGroup(CONFIG_TYPE_APP);
    }
}

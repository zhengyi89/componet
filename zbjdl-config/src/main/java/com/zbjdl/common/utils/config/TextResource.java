package com.zbjdl.common.utils.config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextResource {
    private static Logger logger = LoggerFactory.getLogger(TextResource.class);

    public TextResource() {
    }

    public String getText(String textType, String textKey, String defaultValue) {
        String result = null;

        try {
            result = TextResourceUtils.getText(textType, textKey);
        } catch (Exception var6) {
            logger.warn("getText error : textType[" + textType + "] textKey[" + textKey + "]" + var6.getMessage());
        }

        if (result == null) {
            result = defaultValue;
        }

        return result;
    }

    public String getSysText(String textType, String textKey, String defaultValue) {
        String result = null;

        try {
            result = TextResourceUtils.getTextByConfigType(ConfigurationUtils.CONFIG_TYPE_SYS, textType, textKey);
        } catch (Exception var6) {
            logger.warn("getText error : textType[" + textType + "] textKey[" + textKey + "]" + var6.getMessage());
        }

        if (result == null) {
            result = defaultValue;
        }

        return result;
    }

    public String getAppText(String textType, String textKey, String defaultValue) {
        String result = null;

        try {
            result = TextResourceUtils.getTextByConfigType(ConfigurationUtils.CONFIG_TYPE_APP, textType, textKey);
        } catch (Exception var6) {
            logger.warn("getText error : textType[" + textType + "] textKey[" + textKey + "]" + var6.getMessage());
        }

        if (result == null) {
            result = defaultValue;
        }

        return result;
    }

    public String getText(String textType, String textKey) {
        return this.getText(textType, textKey, (String)null);
    }

    public String getSysText(String textType, String textKey) {
        return this.getSysText(textType, textKey, (String)null);
    }

    public String getAppText(String textType, String textKey) {
        return this.getAppText(textType, textKey, (String)null);
    }

    public Map getTextMap(String type) {
        try {
            return TextResourceUtils.getTextMap(type);
        } catch (Exception var3) {
            logger.warn("getTextMap fail, type[" + type + "] errorMsg[" + var3.getMessage() + "]");
            return null;
        }
    }

    public Map getSysTextMap(String type) {
        try {
            return TextResourceUtils.getTextMapByConfigType(ConfigurationUtils.CONFIG_TYPE_SYS, type);
        } catch (Exception var3) {
            logger.warn("getTextMap fail, type[" + type + "] errorMsg[" + var3.getMessage() + "]");
            return null;
        }
    }

    public Map getAppTextMap(String type) {
        try {
            return TextResourceUtils.getTextMapByConfigType(ConfigurationUtils.CONFIG_TYPE_APP, type);
        } catch (Exception var3) {
            logger.warn("getTextMap fail, type[" + type + "] errorMsg[" + var3.getMessage() + "]");
            return null;
        }
    }

    public Map getTextMap(String type, Object topKey, Object topValue) {
        Map map = this.getTextMap(type);
        if (map == null) {
            return null;
        } else {
            Map result = new LinkedHashMap();
            result.put(topKey, topValue);
            result.putAll(map);
            return result;
        }
    }

    public Map getSysTextMap(String type, Object topKey, Object topValue) {
        Map map = this.getSysTextMap(type);
        if (map == null) {
            return null;
        } else {
            Map result = new LinkedHashMap();
            result.put(topKey, topValue);
            result.putAll(map);
            return result;
        }
    }

    public Map getAppTextMap(String type, Object topKey, Object topValue) {
        Map map = this.getAppTextMap(type);
        if (map == null) {
            return null;
        } else {
            Map result = new LinkedHashMap();
            result.put(topKey, topValue);
            result.putAll(map);
            return result;
        }
    }

    public static String getExceptionMessage(String code) {
        try {
            return TextResourceUtils.getExceptionMessage(code);
        } catch (Exception var2) {
            logger.warn("getExceptionMessage error : code[" + code + "]" + var2.getMessage());
            return null;
        }
    }

    public static String getExceptionMessage(String exceptionClass, String code) {
        try {
            return TextResourceUtils.getExceptionMessage(exceptionClass, code);
        } catch (Exception var3) {
            logger.warn("getExceptionMessage error : code[" + code + "]" + var3.getMessage());
            return null;
        }
    }

    public static String getExceptionCode(String exceptionClass, String code) {
        try {
            return TextResourceUtils.getExceptionCode(exceptionClass, code);
        } catch (Exception var3) {
            logger.warn("getExceptionMessage error : code[" + code + "]" + var3.getMessage());
            return null;
        }
    }

    public String getSimpleConfigValue(String configType, String textKey) {
        ConfigParam configParam = ConfigurationUtils.getConfigParam(configType, textKey);
        return configParam == null ? "" : String.valueOf(configParam.getValue());
    }
}

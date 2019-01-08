package com.zbjdl.common.utils.config.utils;

import com.zbjdl.common.utils.CommonUtils;
import com.zbjdl.common.utils.DateUtils;
import com.zbjdl.common.utils.config.enumtype.ValueDataTypeEnum;
import com.zbjdl.common.utils.config.enumtype.ValueTypeEnum;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
    private static Map<String, String> map = new LinkedHashMap();

    public ConfigUtil() {
    }

    public static String toXmlStr(ValueTypeEnum valueType, Object value) {
        Document doc = DocumentHelper.createDocument();
        Element rootEle = doc.addElement("config-value");
        Object val;
        switch(valueType) {
        case LIST:
            List<Object> data = (List)value;
            Iterator var16 = data.iterator();

            while(var16.hasNext()) {
                Object obj = var16.next();
                Element empEle = rootEle.addElement("value");
                empEle.setText(obj.toString());
            }

            return doc.asXML();
        case MAP:
            Map<String, Object> map = (Map)value;
            Iterator var17 = map.keySet().iterator();

            while(var17.hasNext()) {
                Object obj = var17.next();
                Element empEle = rootEle.addElement("value");
                val = map.get(obj);
                empEle.addAttribute("key", obj.toString());
                empEle.setText(val.toString());
            }

            return doc.asXML();
        case STRUCTURE:
            Map<String, Object> entityMap = (Map)value;
            Iterator var7 = entityMap.keySet().iterator();

            while(true) {
                while(var7.hasNext()) {
                    Object obj = var7.next();
                    val = entityMap.get(obj);
                    Element empEle = rootEle.addElement("value");
                    empEle.addAttribute("key", obj.toString());
                    if (val instanceof Long) {
                        empEle.addAttribute("datatype", ValueDataTypeEnum.INTEGER.name());
                        empEle.setText(val.toString());
                    } else if (val instanceof Double) {
                        empEle.addAttribute("datatype", ValueDataTypeEnum.DOUBLE.name());
                        empEle.setText(val.toString());
                    } else if (val instanceof String) {
                        empEle.addAttribute("datatype", ValueDataTypeEnum.STRING.name());
                        empEle.setText(val.toString());
                    } else if (val instanceof Boolean) {
                        empEle.addAttribute("datatype", ValueDataTypeEnum.BOOLEAN.name());
                        empEle.setText(val.toString());
                    } else if (val instanceof Date) {
                        Date date = (Date)val;
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(13) == 0 && c.get(10) == 0 && c.get(12) == 0) {
                            empEle.addAttribute("datatype", ValueDataTypeEnum.DATE.name());
                            empEle.setText(DateUtils.getShortDateStr(date));
                        } else {
                            empEle.addAttribute("datatype", ValueDataTypeEnum.DATETIME.name());
                            empEle.setText(DateUtils.getLongDateStr((Timestamp)date));
                        }
                    } else {
                        Iterator var12;
                        Object object;
                        Element dataEle;
                        if (val instanceof Map) {
                            Map<String, Object> valueMap = (Map)value;
                            var12 = valueMap.keySet().iterator();

                            while(var12.hasNext()) {
                                object = var12.next();
                                dataEle = empEle.addElement("value");
                                Object valObject = valueMap.get(object);
                                dataEle.addAttribute("key", object.toString());
                                dataEle.setText(valObject.toString());
                            }
                        } else if (val instanceof List) {
                            List<Object> valueList = (List)val;
                            var12 = valueList.iterator();

                            while(var12.hasNext()) {
                                object = var12.next();
                                dataEle = empEle.addElement("value");
                                dataEle.setText(object.toString());
                            }
                        }
                    }
                }

                return doc.asXML();
            }
        default:
            return value.toString();
        }
    }

    public static Object XmlStrToObject(ValueTypeEnum valueType, ValueDataTypeEnum dataType, String value) throws DocumentException {
        if (!valueType.name().equals("VALUE")) {
            Element rootNode = null;

            try {
                rootNode = DocumentHelper.parseText(value).getRootElement();
            } catch (DocumentException var16) {
                throw var16;
            }

            List<Element> elements = rootNode.elements("value");
            switch(valueType) {
            case LIST:
                List<Object> values = new ArrayList();
                Iterator var17 = elements.iterator();

                while(var17.hasNext()) {
                    Element e = (Element)var17.next();
                    String val = e.getTextTrim();
                    values.add(str2Object(dataType, val));
                }

                return values;
            case MAP:
                Map<String, Object> datas = new LinkedHashMap();
                Iterator var18 = elements.iterator();

                while(var18.hasNext()) {
                    Element e = (Element)var18.next();
                    String val = e.getTextTrim();
                    Attribute att = e.attribute("key");
                    String key = att.getValue();
                    datas.put(key, str2Object(dataType, val));
                }

                return datas;
            case STRUCTURE:
                Map<String, Object> dats = new HashMap();

                String val;
                String key;
                ValueDataTypeEnum valueDataType;
                for(Iterator var8 = elements.iterator(); var8.hasNext(); dats.put(key, str2Object(valueDataType, val))) {
                    Element e = (Element)var8.next();
                    val = "";
                    Attribute att = e.attribute("key");
                    key = att.getValue();
                    Attribute dataTypeAttr = e.attribute("datatype");
                    String type = dataTypeAttr.getValue();
                    valueDataType = ValueDataTypeEnum.valueOf(type);
                    if (valueDataType == ValueDataTypeEnum.MAP) {
                        val = e.asXML();
                    } else if (valueDataType == ValueDataTypeEnum.LIST) {
                        val = e.asXML();
                    } else {
                        val = e.getTextTrim();
                    }
                }

                return dats;
            }
        }

        return str2Object(dataType, value);
    }

    public static Object str2Object(ValueDataTypeEnum type, String value) {
        Object v = null;
        String f;
        switch(type) {
        case INTEGER:
            v = Long.valueOf(value);
            break;
        case DOUBLE:
            v = Double.valueOf(value);
            break;
        case DATE:
            f = "yyyy-MM-dd";

            try {
                v = (new SimpleDateFormat(f)).parse(value);
            } catch (ParseException var8) {
                ;
            }
            break;
        case DATETIME:
            f = "yyyy-MM-dd HH:mm";

            try {
                v = (new SimpleDateFormat(f)).parse(value);
            } catch (ParseException var7) {
                ;
            }
            break;
        case BOOLEAN:
            v = Boolean.valueOf(value);
            break;
        case MAP:
            try {
                v = XmlStrToObject(ValueTypeEnum.MAP, type, value);
            } catch (DocumentException var6) {
                logger.error(var6.getMessage(), var6);
            }
            break;
        case LIST:
            try {
                v = XmlStrToObject(ValueTypeEnum.LIST, type, value);
            } catch (DocumentException var5) {
                logger.error(var5.getMessage(), var5);
            }
            break;
        default:
            v = value;
        }

        return v;
    }

    public static synchronized Map<String, String> getSysName() {
        if (map.size() != 0) {
            return map;
        } else {
            Map<String, String> tempMap = CommonUtils.loadProps("config/config-sysname.properties");
            Set<Entry<String, String>> set = tempMap.entrySet();
            Iterator it = set.iterator();

            while(it.hasNext()) {
                Entry entry = (Entry)it.next();

                try {
                    map.put(entry.getKey().toString(), new String(((String)entry.getValue()).getBytes("ISO8859-1"), "utf-8"));
                } catch (UnsupportedEncodingException var5) {
                    ;
                }
            }

            return map;
        }
    }
}

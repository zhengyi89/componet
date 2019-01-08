package com.zbjdl.common.utils.config.utils;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.config.enumtype.ValueDataTypeEnum;
import com.zbjdl.common.utils.config.enumtype.ValueTypeEnum;
import com.zbjdl.common.utils.config.param.ConfigDTO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanXmlUtils<T> {
    private static final Logger logger = LoggerFactory.getLogger(BeanXmlUtils.class);

    public BeanXmlUtils() {
    }

    public String writeXmlDocument(T obj, List<T> list, String Encode) {
        String xmlName = getXmlName();
        XMLWriter writer = null;

        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            File file = new File(xmlName);
            if (file.exists()) {
                file.delete();
            }

            Document document = DocumentHelper.createDocument();
            String rootname = obj.getClass().getSimpleName();
            Element root = document.addElement(rootname + "s");
            Field[] properties = obj.getClass().getDeclaredFields();
            Iterator var12 = list.iterator();

            while(var12.hasNext()) {
                T t = (T) var12.next();
                Element secondRoot = root.addElement(rootname);

                for(int i = 0; i < properties.length; ++i) {
                    if (properties[i].getName().indexOf("serial") == -1) {
                        Method method = t.getClass().getMethod("get" + properties[i].getName().substring(0, 1).toUpperCase() + properties[i].getName().substring(1));
                        Object value = method.invoke(t);
                        if (value != null) {
                            secondRoot.addElement(properties[i].getName()).setText(value.toString());
                        }
                    }
                }
            }

            writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.flush();
        } catch (Exception var26) {
            logger.error("XML文件写入失败" + var26.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException var25) {
                    ;
                }
            }

        }

        return xmlName;
    }

    public List<T> readXmlFromFileName(String XMLPathAndName, T t) {
        new ArrayList();
        File f = new File(XMLPathAndName);
        List<T> list = this.readXmlFromFileStreams(f, t);
        return list;
    }

    public List<T> readXmlFromFileStreams(File file, T t) {
        ArrayList list = new ArrayList();

        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(file);
            Element root = doc.getRootElement();
            Field[] atr = t.getClass().getDeclaredFields();
            Iterator it = root.elementIterator(t.getClass().getSimpleName());

            while(it.hasNext()) {
                Element child = (Element)it.next();
                T obj = (T) t.getClass().newInstance();

                for(int j = 0; j < atr.length; ++j) {
                    if (atr[j].getName().indexOf("serial") == -1) {
                        Method method = obj.getClass().getMethod("set" + atr[j].getName().substring(0, 1).toUpperCase() + atr[j].getName().substring(1), atr[j].getType());
                        if (atr[j].getType() != Date.class && child.elementText(atr[j].getName()) != null) {
                            if (atr[j].getType() == Long.class) {
                                Long value = Long.parseLong(child.elementText(atr[j].getName()));
                                method.invoke(obj, value);
                            } else if (atr[j].getType() == ValueDataTypeEnum.class) {
                                method.invoke(obj, ValueDataTypeEnum.valueOf(child.elementText(atr[j].getName())));
                            } else if (atr[j].getType() == ValueTypeEnum.class) {
                                method.invoke(obj, ValueTypeEnum.valueOf(child.elementText(atr[j].getName())));
                            } else if (atr[j].getType() == Boolean.class) {
                                method.invoke(obj, Boolean.valueOf(child.elementText(atr[j].getName())));
                            } else {
                                method.invoke(obj, child.elementText(atr[j].getName()));
                            }
                        }
                    }
                }

                list.add(obj);
            }
        } catch (Exception var14) {
            logger.error("XML读取失败" + var14.getMessage());
        }

        return list;
    }

    private static String getXmlName() {
        String dir = System.getProperty("java.io.tmpdir");
        if (CheckUtils.isEmpty(dir)) {
            dir = "/apps/data/java-tmpdir/";
        }

        if (!dir.endsWith("/")) {
            dir = dir + "/";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = sdf.format(new Date()) + ".xml";
        return dir + fileName;
    }

    public static void main(String[] args) {
        BeanXmlUtils<ConfigDTO> beanXmlUtils = new BeanXmlUtils();
        List list = beanXmlUtils.readXmlFromFileStreams(new File("d:/20120423150325.xml"), new ConfigDTO());

        for(int i = 0; i < list.size(); ++i) {
            ConfigDTO configDTO = (ConfigDTO)list.get(i);
            System.out.println(configDTO.getNamespace() + "  " + configDTO.getSystemId());
        }

    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.conf;

import com.zbjdl.common.utils.event.lang.uri.WebURI;
import com.zbjdl.common.utils.event.text.StringMatchUtils;
import com.zbjdl.common.utils.event.utils.PropertiesEx;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceVipManager {
    private static final Logger logger = LoggerFactory.getLogger(ServiceVipManager.class);
    private Map<String, ServiceVip> vips = new HashMap();
    private static final String ENCODE = "UTF-8";

    public ServiceVipManager() {
    }

    public void init() {
        String path = SystemConfigUtils.getProperty("vip.hosts");
        if (StringUtils.isNotBlank(path)) {
            this.parserSingleFile(new File(path));
        } else {
            logger.warn("don't define vip hosts.");
        }

    }

    public Map<String, ServiceVip> getVipsByType(String type) {
        Map<String, ServiceVip> tempVips = new HashMap();
        Iterator<String> keys = this.vips.keySet().iterator();
        String temp = null;
        ServiceVip vip = null;

        while(keys.hasNext()) {
            temp = (String)keys.next();
            vip = (ServiceVip)this.vips.get(temp);
            if (StringUtils.equals(type, vip.getType())) {
                tempVips.put(temp, vip);
            }
        }

        return tempVips;
    }

    public Map<String, ServiceVip> getVips() {
        return this.vips;
    }

    public ServiceVip findServiceVip(WebURI uri) {
        if (null != uri) {
            String key = uri.getHost() + ":" + uri.getPort();
            if (this.vips.containsKey(key)) {
                return (ServiceVip)this.vips.get(key);
            }

            Iterator<String> keys = this.vips.keySet().iterator();
            String temp = null;

            while(keys.hasNext()) {
                temp = (String)keys.next();
                if (StringMatchUtils.stringAntMatch(key, temp)) {
                    return (ServiceVip)this.vips.get(temp);
                }
            }
        }

        return null;
    }

    private void parserSingleFile(File file) {
        Properties systemProperties = new PropertiesEx();
        InputStream in = null;
        InputStreamReader reader = null;

        try {
            in = new FileInputStream(file);
            reader = new InputStreamReader(in, "UTF-8");
            systemProperties.load(reader);
            Iterator<Object> keys = systemProperties.keySet().iterator();
            String key = null;
            String value = null;

            while(keys.hasNext()) {
                key = StringUtils.trim(keys.next().toString());
                value = StringUtils.trim(systemProperties.getProperty(key));
                if (key.startsWith("host.")) {
                    key = key.substring(5);
                    ServiceVip vip = new ServiceVip(key, value, systemProperties);
                    this.vips.put(vip.getHost(), vip);
                    logger.info("VIP = " + vip);
                }
            }
        } catch (Exception var21) {
            logger.error("parser error.", var21);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception var20) {
                    ;
                }
            }

            if (null != reader) {
                try {
                    reader.close();
                } catch (Exception var19) {
                    ;
                }
            }

        }

    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.conf;

import com.zbjdl.common.utils.event.text.StringUtilsEx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceVip {
    private static final Logger logger = LoggerFactory.getLogger(ServiceVip.class);
    private String name;
    private String host;
    private List<ServiceIP> ips = new ArrayList();
    private String lb = "RoundRobin";
    private String type = "http";
    private Map<String, String> properties;

    public ServiceVip(String name, String host, Properties context) {
        this.name = name;
        int p1 = host.indexOf("?");
        if (p1 > 0) {
            this.host = host.substring(0, p1);
            this.properties = StringUtilsEx.uriQueryToMap(host.substring(p1 + 1));
        } else {
            this.host = host;
            this.properties = Collections.emptyMap();
        }

        Enumeration<Object> keys = context.keys();
        Object temp = null;
        String key = null;
        String value = null;

        while(true) {
            while(true) {
                do {
                    while(true) {
                        do {
                            do {
                                if (!keys.hasMoreElements()) {
                                    return;
                                }

                                temp = keys.nextElement();
                            } while(null == temp);

                            key = temp.toString();
                        } while(!key.startsWith(name));

                        value = context.getProperty(key);
                        logger.info("VIP HOSTS CONF : " + name + " = " + value);
                        if (key.contains(".ip.")) {
                            break;
                        }

                        if (key.contains(".lb")) {
                            if (StringUtils.isNotBlank(value)) {
                                this.lb = value;
                            }
                        } else if (key.contains(".type") && StringUtils.isNotBlank(value)) {
                            this.type = value;
                        }
                    }
                } while(!StringUtils.isNotBlank(value));

                int p = value.indexOf(":");
                String domain = value.substring(0, p);
                int p2 = domain.indexOf(".");
                if (p2 > 0) {
                    domain = domain.substring(0, p2);
                    p = p2;
                }

                String[] dpart = StringUtils.split(domain, "-");
                if (dpart.length == 3) {
                    String[] groups = this.parser(dpart[1], domain);
                    String[] members = this.parser(dpart[2], domain);

                    for(int i = 0; i < groups.length; ++i) {
                        for(int j = 0; j < members.length; ++j) {
                            this.ips.add(new ServiceIP(dpart[0] + "-" + groups[i] + "-" + members[j] + value.substring(p)));
                        }
                    }
                } else {
                    this.ips.add(new ServiceIP(value));
                }
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public String getHost() {
        return this.host;
    }

    public List<ServiceIP> getIps() {
        return this.ips;
    }

    public String getLb() {
        return this.lb;
    }

    public String getType() {
        return this.type;
    }

    public String getProperty(String key) {
        return (String)this.properties.get(key);
    }

    private String[] parser(String group, String domain) {
        String[] groups = new String[0];
        if (group.startsWith("[") && group.endsWith("]")) {
            group = group.substring(1, group.length() - 1);
            if (group.indexOf("..") > 0) {
                int p = group.indexOf("..");
                String begin = group.substring(0, p);
                String end = group.substring(p + 2);
                int pb = NumberUtils.toInt(begin, 1);
                int pe = NumberUtils.toInt(end, 1);
                groups = new String[pe - pb + 1];

                for(int i = 0; i < groups.length; ++i) {
                    groups[i] = String.valueOf(pb + i);
                }
            } else {
                groups = StringUtils.split(group, ",");
            }
        } else if (!group.startsWith("[") && !group.endsWith("]")) {
            groups = new String[]{group};
        } else {
            logger.error("不符合格式的ip定义：" + domain);
        }

        return groups;
    }

    public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
        } catch (Exception var2) {
            return super.toString();
        }
    }
}

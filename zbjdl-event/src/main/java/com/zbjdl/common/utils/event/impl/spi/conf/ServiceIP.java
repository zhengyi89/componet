//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.conf;

import com.zbjdl.common.utils.event.text.StringUtilsEx;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ServiceIP {
    private String ip;
    private Map<String, String> properties;

    public ServiceIP(String ipvalue) {
        int p = ipvalue.indexOf("?");
        if (p > 0) {
            this.ip = ipvalue.substring(0, p);
            this.properties = StringUtilsEx.uriQueryToMap(ipvalue.substring(p + 1));
        } else {
            this.ip = ipvalue;
            this.properties = Collections.emptyMap();
        }

    }

    public String getIp() {
        return this.ip;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
        } catch (Exception var2) {
            return super.toString();
        }
    }
}

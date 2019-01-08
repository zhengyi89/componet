//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.uri;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.zbjdl.common.utils.event.lang.hessian.ext.AtomSerialization;
import com.zbjdl.common.utils.event.model.ID;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class WebURI extends ID implements Serializable, AtomSerialization {
    private static final transient long serialVersionUID = -7709381993746266584L;
    private transient URI uri;
    private transient Map<String, Object> parameterMap;
    private transient boolean hasInit;
    protected String domain;
    protected String version;
    protected String rawPath;
    private transient boolean ignoreCache;
    private transient boolean debugTemplate;
    private transient String encoding;

    public WebURI() {
        this.hasInit = false;
        this.parameterMap = new HashMap();
        this.ignoreCache = false;
        this.debugTemplate = false;
        this.encoding = null;
    }

    public WebURI(WebURI old) {
        this.hasInit = false;
        this.uri = old.uri;
        this.parameterMap = old.parameterMap;
        this.domain = old.domain;
        this.version = old.version;
        this.rawPath = old.rawPath;
        this.hasInit = old.hasInit;
    }

    public WebURI(String rawRequestURI) {
        this();
        this.rawPath = rawRequestURI;
        this.parser();
    }

    public boolean isSameProduct(WebURI webURI) {
        return true;
    }

    public String getScheme() {
        this.parser();
        return this.uri.getScheme();
    }

    public String getRequestPath() {
        this.parser();
        if (null == this.uri) {
            int p = this.rawPath.indexOf("?");
            return p > 0 ? this.rawPath.substring(0, p) : this.rawPath;
        } else {
            return this.uri.getPath();
        }
    }

    public String getRawPathWithoutParameter() {
        this.parser();
        int p = this.rawPath.indexOf("?");
        return p > 0 ? this.rawPath.substring(0, p) : this.rawPath;
    }

    public String getRawPath() {
        this.parser();
        return this.rawPath;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHost() {
        this.parser();
        return this.uri.getHost();
    }

    public int getPort() {
        this.parser();
        return this.uri.getPort();
    }

    public boolean isRemtoeURI() {
        this.parser();
        return StringUtils.isNotBlank(this.uri.getScheme()) && StringUtils.isNotBlank(this.uri.getHost());
    }

    public boolean isSupportedPage(String supportedExtension) {
        this.parser();
        String name = this.getRequestPath();
        if (StringUtils.isBlank(name)) {
            return false;
        } else {
            int p = name.lastIndexOf(".");
            if (p < 0) {
                return false;
            } else {
                name = name.substring(p);
                return StringUtils.containsIgnoreCase(supportedExtension, name);
            }
        }
    }

    public String getVersionPath() {
        if (null != this.domain && null != this.version) {
            return this.domain + "@" + this.getRequestPath() + "#" + this.version;
        } else if (null != this.domain && null == this.version) {
            return this.domain + "@" + this.getRequestPath();
        } else {
            return null != this.version ? this.getRequestPath() + "#" + this.version : this.getRequestPath();
        }
    }

    private String getRawVersionPath() {
        if (null != this.domain && null != this.version) {
            return this.domain + "@" + this.rawPath + "#" + this.version;
        } else if (null != this.domain && null == this.version) {
            return this.domain + "@" + this.rawPath;
        } else {
            return null != this.version ? this.rawPath + "#" + this.version : this.rawPath;
        }
    }

    public Map<String, Object> getParameterMap() {
        this.parser();
        return this.parameterMap;
    }

    public Object getParameter(String key) {
        this.parser();
        return this.parameterMap.get(key);
    }

    public boolean equals(Object obj) {
        try {
            this.parser();
            WebURI getURI = (WebURI)obj;
            if (StringUtils.equals(this.getRawPath(), getURI.getRawPath())) {
                return true;
            }
        } catch (Exception var3) {
            ;
        }

        return false;
    }

    public int hashCode() {
        this.parser();
        return this.getRawPath().hashCode();
    }

    private void parser() {
        if (null == this.uri) {
            this.hasInit = false;
        }

        if (!this.hasInit) {
            String rawRequestURI = this.rawPath;
            this.hasInit = true;

            try {
                this.uri = new URI(rawRequestURI);
                if (null != this.uri.getQuery()) {
                    String paramString = this.uri.getQuery();
                    if (StringUtils.isNotBlank(paramString)) {
                        String[] params = StringUtils.split(paramString, "&");
                        String key = null;
                        String value = null;
                        String[] var7 = params;
                        int var8 = params.length;

                        for(int var9 = 0; var9 < var8; ++var9) {
                            String param = var7[var9];
                            int p = param.indexOf("=");
                            key = param.substring(0, p);
                            value = param.substring(p + 1);
                            this.parameterMap.put(key, value);
                        }
                    }
                }
            } catch (Exception var11) {
                ;
            }
        }

    }

    public boolean isIgnoreCache() {
        return this.ignoreCache;
    }

    public void setIgnoreCache(boolean ignoreCache) {
        this.ignoreCache = ignoreCache;
    }

    public boolean isDebugTemplate() {
        return this.debugTemplate;
    }

    public void setDebugTemplate(boolean debugTemplate) {
        this.debugTemplate = debugTemplate;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void reset() {
        this.ignoreCache = false;
        this.debugTemplate = false;
        this.encoding = null;
    }

    public void putAll(Map<String, Object> m) {
        this.parameterMap.putAll(m);
    }

    public void put(String key, String value) {
        this.parameterMap.put(key, value);
    }

    public boolean isFormActionSubmit() {
        this.parser();
        return this.rawPath.indexOf("/action/") > 0;
    }

    public int getBizCode() {
        return 0;
    }

    public String getID() {
        return this.getRawPath();
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("raw=").append(this.getRawPath());
        buff.append(" | versionPath=").append(this.getVersionPath());
        return buff.toString();
    }

    public void readObjectData(AbstractHessianInput buffer) throws IOException {
        String temp = buffer.readString();
        if (null != temp) {
            int p = temp.indexOf("@");
            if (p > -1) {
                this.domain = temp.substring(0, p);
                temp = temp.substring(p + 1);
            }

            p = temp.lastIndexOf("#");
            if (p > -1) {
                this.rawPath = temp.substring(0, p);
                this.version = temp.substring(p + 1);
            } else {
                this.rawPath = temp;
            }
        } else {
            this.rawPath = temp;
        }

    }

    public void writeObjectData(AbstractHessianOutput buffer) throws IOException {
        buffer.writeString(this.getRawVersionPath());
    }
}

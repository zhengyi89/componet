//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.runtime;

import com.zbjdl.common.utils.event.model.BaseModel;

public class ProductEnvironment extends BaseModel {
    private static final long serialVersionUID = 2066683159811381116L;
    private String clientIp;
    private String clientDeviceName;
    private String serverIp;
    private String serverName;
    private String pcid;

    public ProductEnvironment() {
    }

    public ProductEnvironment(String clientIp, String serverIp, String serverName) {
        this.clientIp = clientIp;
        this.serverIp = serverIp;
        this.serverName = serverName;
    }

    public String getClientIp() {
        return this.clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getClientDeviceName() {
        return this.clientDeviceName;
    }

    public void setClientDeviceName(String clientDeviceName) {
        this.clientDeviceName = clientDeviceName;
    }

    public String getPcid() {
        return this.pcid;
    }

    public void setPcid(String pcid) {
        this.pcid = pcid;
    }
}

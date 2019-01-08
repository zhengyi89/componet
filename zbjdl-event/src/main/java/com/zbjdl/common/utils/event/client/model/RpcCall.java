//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.client.model;

import com.zbjdl.common.utils.event.lang.uri.WebURI;

public class RpcCall extends Call<Object[]> {
    private static final long serialVersionUID = -6876505205256770409L;

    public RpcCall() {
    }

    public RpcCall(WebURI serviceUri, Object[] parameter) {
        this.serviceUri = serviceUri;
        this.parameter = parameter;
        this.code = CodeEnum.HESSIEN.code();
    }
}

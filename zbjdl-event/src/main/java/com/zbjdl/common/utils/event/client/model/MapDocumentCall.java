//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.client.model;

import com.zbjdl.common.utils.event.lang.uri.WebURI;
import java.util.Map;

public class MapDocumentCall extends DocumentCall<Map<String, Object>> {
    private static final long serialVersionUID = -1032563855276919067L;

    public MapDocumentCall() {
    }

    private MapDocumentCall(WebURI serviceUri, int code, Map<String, Object> parameter) {
        this.serviceUri = serviceUri;
        this.code = code;
        this.parameter = parameter;
    }

    public MapDocumentCall(WebURI serviceUri, Map<String, Object> parameter) {
        this(serviceUri, CodeEnum.HTTP.code(), parameter);
    }
}

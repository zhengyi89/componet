//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.client.model;

import com.zbjdl.common.utils.event.lang.uri.WebURI;

public class StringDocumentCall extends DocumentCall<String> {
    private static final long serialVersionUID = 1691994739541295680L;

    public StringDocumentCall() {
    }

    private StringDocumentCall(WebURI serviceUri, int code, String parameter) {
        this.serviceUri = serviceUri;
        this.code = code;
        this.parameter = parameter;
    }

    public StringDocumentCall(WebURI serviceUri, String parameter) {
        this(serviceUri, CodeEnum.HTTP.code(), parameter);
    }
}

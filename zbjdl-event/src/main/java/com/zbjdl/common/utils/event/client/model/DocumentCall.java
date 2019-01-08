//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.client.model;

public abstract class DocumentCall<T> extends Call<T> {
    private static final long serialVersionUID = 6264292278045752738L;
    private String encoding = "UTF-8";

    public DocumentCall() {
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}

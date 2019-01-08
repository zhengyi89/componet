//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.uri;

public class WebURIFactory {
    private static WebURIFactory webURIFactory = new WebURIFactory();

    public static WebURIFactory singleWebURIFactory() {
        return webURIFactory;
    }

    public WebURIFactory() {
    }

    public void init() {
        webURIFactory = this;
    }

    public WebURI getWebURI(String uri) {
        return new WebURI(uri);
    }
}

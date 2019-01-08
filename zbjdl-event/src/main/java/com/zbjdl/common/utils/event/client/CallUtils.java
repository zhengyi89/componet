//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.client;

import com.zbjdl.common.utils.event.lang.jndi.ObjectBindContext;
import com.zbjdl.common.utils.event.lang.uri.WebURI;

public class CallUtils {
    public CallUtils() {
    }

    public static String toCallStringURI(String url, String methodName) {
        return url.indexOf("?") > 0 ? url + "&actionName=" + methodName : url + "?actionName=" + methodName;
    }

    public static WebURI toCallObjectURI(String url, String methodName) {
        return new WebURI(toCallStringURI(url, methodName));
    }

    public static String toVmURI(String path) {
        return path.startsWith("/") ? "jndivm://www.yeepay.com" + path : "jndivm://www.yeepay.com/" + path;
    }

    public static void bindingVmService(WebURI serviceUri, Object service) {
        ObjectBindContext.bind(serviceUri, service);
    }
}

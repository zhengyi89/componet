//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLUtils {
    public URLUtils() {
    }

    public static String encodeUrl(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (Exception var3) {
            return url;
        }
    }

    public static String decodeUrl(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (Exception var3) {
            return url;
        }
    }

    public static String optionAppend(String url, String prefix) {
        return !url.startsWith("http:") && !url.startsWith("https:") ? prefix + url : url;
    }
}

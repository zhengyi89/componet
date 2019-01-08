//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.http;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;

public class IgnoreCookiesSpec implements CookieSpec {
    List<Cookie> cookies = new ArrayList(0);
    List<Header> headers = new ArrayList(0);

    public IgnoreCookiesSpec() {
    }

    public List<Header> formatCookies(List<Cookie> cookies) {
        return this.headers;
    }

    public int getVersion() {
        return 0;
    }

    public Header getVersionHeader() {
        return null;
    }

    public boolean match(Cookie cookie, CookieOrigin origin) {
        return true;
    }

    public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
        return this.cookies;
    }

    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
    }
}

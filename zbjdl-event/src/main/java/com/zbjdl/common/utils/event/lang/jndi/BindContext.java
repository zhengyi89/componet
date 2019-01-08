//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.jndi;

import com.zbjdl.common.utils.event.lang.uri.WebURI;

public interface BindContext {
    void bind(WebURI var1, Object var2);

    Object lookup(WebURI var1);

    void unbind(WebURI var1);

    void bind(String var1, Object var2);

    Object lookup(String var1);

    void unbind(String var1);
}

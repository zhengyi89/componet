//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.jndi;

import com.zbjdl.common.utils.event.lang.uri.WebURI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectBindContext {
    private static final Logger logger = LoggerFactory.getLogger(ObjectBindContext.class);
    private static BindContext rootContext = new ObjectBindContext.MapBindContext();
    private static Map<String, BindContext> dirContext = new HashMap();

    public ObjectBindContext() {
    }

    public static void bind(WebURI name, Object provider) {
        rootContext.bind(name, provider);
    }

    public static void bind(String name, Object provider) {
        rootContext.bind(name, provider);
    }

    public static void bindDir(String name, BindContext provider) {
        dirContext.put(name, provider);
    }

    public static Object lookup(WebURI name) {
        Object obj = rootContext.lookup(name);
        if (null != obj) {
            return obj;
        } else {
            Iterator iter = dirContext.values().iterator();

            while(iter.hasNext()) {
                obj = ((BindContext)iter.next()).lookup(name);
                if (null != obj) {
                    break;
                }
            }

            return obj;
        }
    }

    public static Object lookup(String name) {
        Object obj = rootContext.lookup(name);
        if (null != obj) {
            return obj;
        } else {
            Iterator iter = dirContext.values().iterator();

            while(iter.hasNext()) {
                obj = ((BindContext)iter.next()).lookup(name);
                if (null != obj) {
                    break;
                }
            }

            return obj;
        }
    }

    public static void unbind(WebURI name) {
        rootContext.unbind(name);
    }

    public static void unbind(String name) {
        rootContext.unbind(name);
    }

    public static void unbindDir(String name) {
        dirContext.remove(name);
    }

    private static class MapBindContext implements BindContext {
        private static Map<String, Object> contextCache = new HashMap();

        private MapBindContext() {
        }

        public void bind(WebURI webURI, Object provider) {
            this.bind(webURI.getRequestPath(), provider);
        }

        public Object lookup(WebURI webURI) {
            return this.lookup(webURI.getRequestPath());
        }

        public void unbind(WebURI webURI) {
            this.unbind(webURI.getRequestPath());
        }

        public void bind(String name, Object provider) {
            if (ObjectBindContext.logger.isInfoEnabled()) {
                ObjectBindContext.logger.info("name = " + name + " | provider = " + provider);
            }

            if (!contextCache.containsKey(name)) {
                contextCache.put(name, provider);
            }

        }

        public Object lookup(String name) {
            if (ObjectBindContext.logger.isDebugEnabled()) {
                ObjectBindContext.logger.debug("name = " + name);
            }

            return contextCache.containsKey(name) ? contextCache.get(name) : null;
        }

        public void unbind(String name) {
            contextCache.remove(name);
        }
    }
}

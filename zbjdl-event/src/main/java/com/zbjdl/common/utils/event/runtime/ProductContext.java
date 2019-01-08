//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.runtime;

import com.zbjdl.common.utils.event.model.BaseModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductContext extends BaseModel {
    private static final long serialVersionUID = -3135679560010581418L;
    private ProductRuntime productRuntime;
    private transient Map<String, Object> context = null;
    private transient ProductLog productLog;
    private transient AtomicInteger count;

    public ProductContext() {
        this.productRuntime = new ProductRuntime();
    }

    public ProductContext(ProductRuntime productRuntime) {
        this.productRuntime = productRuntime;
    }

    public ProductRuntime getProductRuntime() {
        return this.productRuntime;
    }

    public void setProductRuntime(ProductRuntime productRuntime) {
        this.productRuntime = productRuntime;
    }

    public ProductLog getProductLog() {
        if (null == this.productLog) {
            this.productLog = new ProductLog();
        }

        return this.productLog;
    }

    public void initProductLog(ProductLog plog) {
        this.productLog = plog;
    }

    public void put(String key, Object value) {
        if (null == this.context) {
            this.context = Collections.synchronizedMap(new HashMap());
        }

        this.context.put(key, value);
    }

    public void putAll(Map<String, Object> datas) {
        if (null == this.context) {
            this.context = Collections.synchronizedMap(new HashMap());
        }

        this.context.putAll(datas);
    }

    public Object find(String key) {
        return null == this.context ? null : this.context.get(key);
    }

    public boolean contains(String key) {
        return null == this.context ? false : this.context.containsKey(key);
    }

    public void remove(String key) {
        if (null != this.context) {
            this.context.remove(key);
        }
    }

    public void clean() {
        if (null != this.context) {
            this.context.clear();
        }

    }

    public void begin() {
        if (null == this.count) {
            this.count = new AtomicInteger();
        }

        this.count.incrementAndGet();
    }

    public void end() {
        if (null != this.count) {
            int p = this.count.decrementAndGet();
            if (p < 1) {
                this.clean();
            }
        }

    }

    public void ignoreCache() {
        if (null != this.productRuntime) {
            this.productRuntime.setIgnoreCache(true);
        }

    }

    public boolean isIgnoreCache() {
        return null != this.productRuntime ? this.productRuntime.isIgnoreCache() : false;
    }
}

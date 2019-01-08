//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.http;

import com.zbjdl.common.utils.event.impl.spi.conf.ServiceVip;
import com.zbjdl.common.utils.event.impl.spi.conf.ServiceVipManager;
import com.zbjdl.common.utils.event.impl.spi.lb.LoadBalancer;
import com.zbjdl.common.utils.event.impl.spi.lb.RoundRobinLoadBalancer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpLoadBalancerManager {
    private static final Logger logger = LoggerFactory.getLogger(HttpLoadBalancerManager.class);
    private ServiceVipManager serviceVipManager;
    private List<HttpProcessorFactory> factorys = new ArrayList();
    private HttpClientProxy httpClientProxy;

    public HttpLoadBalancerManager() {
    }

    public void init() {
        Map<String, ServiceVip> vipMaps = this.serviceVipManager.getVipsByType("http");
        Iterator values = vipMaps.values().iterator();

        while(values.hasNext()) {
            ServiceVip vip = (ServiceVip)values.next();
            String className = "com.zbjdl.common.utils.event.impl.spi.lb." + vip.getLb() + "LoadBalancer";
            Object obj = null;

            try {
                obj = Class.forName(className).newInstance();
            } catch (Exception var8) {
                logger.error("", var8);
                obj = new RoundRobinLoadBalancer();
            }

            LoadBalancer lb = (LoadBalancer)obj;
            this.httpClientProxy.registLB(vip.getName(), lb);
            HttpProcessorFactory factory = new HttpProcessorFactory(lb, vip, this.httpClientProxy);
            factory.start();
            this.factorys.add(factory);
        }

    }

    public void destroy() {
        Iterator var1 = this.factorys.iterator();

        while(var1.hasNext()) {
            HttpProcessorFactory factory = (HttpProcessorFactory)var1.next();
            factory.stop();
        }

    }

    public void setServiceVipManager(ServiceVipManager serviceVipManager) {
        this.serviceVipManager = serviceVipManager;
    }

    public void setHttpClientProxy(HttpClientProxy httpClientProxy) {
        this.httpClientProxy = httpClientProxy;
    }
}

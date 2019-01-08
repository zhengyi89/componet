//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.http;

import com.zbjdl.common.utils.event.impl.spi.conf.ServiceIP;
import com.zbjdl.common.utils.event.impl.spi.conf.ServiceVip;
import com.zbjdl.common.utils.event.impl.spi.lb.LoadBalancer;
import com.zbjdl.common.utils.event.utils.ScheduledTaskUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProcessorFactory implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HttpProcessorFactory.class);
    private ServiceVip vip;
    private LoadBalancer lb;
    private List<HttpProcessor> httpProcessorList;

    public HttpProcessorFactory(LoadBalancer lb, ServiceVip vip, HttpClientProxy httpClientProxy) {
        this.lb = lb;
        this.vip = vip;
        this.httpProcessorList = new ArrayList();
        Iterator var4 = vip.getIps().iterator();

        while(var4.hasNext()) {
            ServiceIP ip = (ServiceIP)var4.next();
            HttpProcessor process = new HttpProcessor(ip, httpClientProxy);
            process.start();
            this.httpProcessorList.add(process);
        }

    }

    public void start() {
        long interval = 15000L;
        String temp = this.vip.getProperty("check.interval");
        interval = NumberUtils.toLong(temp, interval);
        ScheduledTaskUtils.scheduleWithFixedDelay(this, 0L, interval);
    }

    public void stop() {
        Iterator var1 = this.httpProcessorList.iterator();

        while(var1.hasNext()) {
            HttpProcessor hp = (HttpProcessor)var1.next();
            hp.stop();
        }

    }

    public void run() {
        try {
            Iterator var1 = this.httpProcessorList.iterator();

            while(var1.hasNext()) {
                HttpProcessor hp = (HttpProcessor)var1.next();
                if (!hp.healthCheck()) {
                    this.lb.removeProcessor(hp);
                } else {
                    this.lb.addProcessor(hp);
                }
            }
        } catch (Exception var3) {
            logger.error(var3.getMessage(), var3);
        }

    }
}

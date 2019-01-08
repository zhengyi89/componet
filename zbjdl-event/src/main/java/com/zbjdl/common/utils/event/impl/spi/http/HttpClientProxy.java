//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.http;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.impl.spi.conf.ServiceIP;
import com.zbjdl.common.utils.event.impl.spi.conf.ServiceVip;
import com.zbjdl.common.utils.event.impl.spi.conf.ServiceVipManager;
import com.zbjdl.common.utils.event.impl.spi.lb.LoadBalancer;
import com.zbjdl.common.utils.event.lang.uri.WebURI;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientProxy {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientProxy.class);
    private DefaultHttpClient httpclient = null;
    private Map<String, LoadBalancer> lbs = new HashMap();
    private HttpProcessor defaultHttpProcessor = new HttpProcessor((ServiceIP)null, this);
    private ServiceVipManager serviceVipManager;
    private int connPerRouteCount = 100;

    public HttpClientProxy() {
    }

    public void registLB(String name, LoadBalancer lb) {
        this.lbs.put(name, lb);
    }

    public void init() {
        this.connPerRouteCount = SystemConfigUtils.getIntProperty("http.client.conn.count.per.route", this.connPerRouteCount);
        HttpParams params = new BasicHttpParams();
        Map<String, ServiceVip> vipMaps = this.serviceVipManager.getVipsByType("http");
        Iterator<ServiceVip> values = vipMaps.values().iterator();

        int count;
        ServiceVip vip;
        for(count = 0; values.hasNext(); count += vip.getIps().size()) {
            vip = (ServiceVip)values.next();
        }

        count *= this.connPerRouteCount;
        if (count < this.connPerRouteCount) {
            count = this.connPerRouteCount;
        }

        ConnManagerParams.setMaxTotalConnections(params, count);
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(this.connPerRouteCount);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setSoTimeout(params, 30000);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        this.httpclient = new DefaultHttpClient(cm, params);
        this.httpclient.getCookieSpecs().register("ignore", new HttpClientProxy.IgnoreCookieSpecFactory());
        this.httpclient.getParams().setParameter("http.protocol.cookie-policy", "ignore");
        this.httpclient.setKeepAliveStrategy(new HttpClientProxy.LongConnectionKeepAliveStrategy());
    }

    public void destroy() {
        this.httpclient.getConnectionManager().shutdown();
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.httpclient.execute(request, responseHandler);
    }

    public String getRawPath(WebURI webURI) {
        HttpProcessor process = this.findHttpProcessor(webURI, (SokulaEvent)null);
        if (null == process) {
            throw new EventRuntimeException(99001003, "remote server don't valide. URI: " + webURI.getRawPath());
        } else {
            String path = process.process(webURI);
            if (logger.isDebugEnabled()) {
                logger.debug("raw : " + webURI.getRawPath() + " | blance : " + path);
            }

            return path;
        }
    }

    private HttpProcessor findHttpProcessor(WebURI webURI, SokulaEvent event) {
        ServiceVip serviceVip = this.serviceVipManager.findServiceVip(webURI);
        if (null != serviceVip && null != this.lbs) {
            LoadBalancer lb = (LoadBalancer)this.lbs.get(serviceVip.getName());
            return (HttpProcessor)lb.getProcessor(event);
        } else {
            return this.defaultHttpProcessor;
        }
    }

    public void setServiceVipManager(ServiceVipManager serviceVipManager) {
        this.serviceVipManager = serviceVipManager;
    }

    private class LongConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
        private LongConnectionKeepAliveStrategy() {
        }

        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            return 180000L;
        }
    }

    private class IgnoreCookieSpecFactory implements CookieSpecFactory {
        private IgnoreCookieSpecFactory() {
        }

        public CookieSpec newInstance(HttpParams params) {
            return new IgnoreCookiesSpec();
        }
    }
}

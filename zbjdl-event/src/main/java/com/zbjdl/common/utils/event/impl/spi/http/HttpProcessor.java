//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.http;

import com.zbjdl.common.utils.event.impl.spi.conf.ServiceIP;
import com.zbjdl.common.utils.event.impl.spi.lb.Processor;
import com.zbjdl.common.utils.event.lang.uri.WebURI;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(HttpProcessor.class);
    private ServiceIP serviceIp;
    private int errorCount = 0;
    private HttpClientProxy httpClientProxy;
    private ReentrantLock checkLock = new ReentrantLock();
    private HttpProcessor.ByteArrayResponseHandler byteArrayResponseHandler = new HttpProcessor.ByteArrayResponseHandler();

    public HttpProcessor(ServiceIP serviceIp, HttpClientProxy httpClientProxy) {
        this.serviceIp = serviceIp;
        this.httpClientProxy = httpClientProxy;
    }

    public String process(WebURI webURI) {
        String rawPath = webURI.getRawPath();
        if (null == this.serviceIp) {
            return rawPath.startsWith("spring") ? "http://" + rawPath.substring(9) : rawPath;
        } else {
            int p1 = rawPath.indexOf("://");
            int p2 = rawPath.indexOf("/", p1 + 3);
            if (p1 > 0 && p2 > 0) {
                if (rawPath.startsWith("spring")) {
                    rawPath = "http://" + this.serviceIp.getIp() + rawPath.substring(p2);
                } else {
                    rawPath = rawPath.substring(0, p1 + 3) + this.serviceIp.getIp() + rawPath.substring(p2);
                }
            }

            return rawPath;
        }
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean healthCheck() {
        if (this.serviceIp != null && this.serviceIp.getProperties().containsKey("check.url")) {
            this.checkLock.lock();
            boolean result = false;

            try {
                String checkUrl = this.serviceIp.getIp();
                if (checkUrl.indexOf("://") < 0) {
                    checkUrl = "http://" + checkUrl;
                }

                checkUrl = checkUrl + (String)this.serviceIp.getProperties().get("check.url");
                ++this.errorCount;
                if (this.errorCount % 100000 == 1 && logger.isDebugEnabled()) {
                    logger.debug("healthCheck url : " + checkUrl);
                }

                HttpGet httpget = new HttpGet(StringUtils.trim(checkUrl));
                byte[] content = (byte[])this.httpClientProxy.execute(httpget, this.byteArrayResponseHandler);
                if (null != content) {
                    result = true;
                }
            } catch (Exception var8) {
                ++this.errorCount;
                if (this.errorCount % 100000 == 1) {
                    logger.error("", var8);
                }
            } finally {
                this.checkLock.unlock();
            }

            return result;
        } else {
            return true;
        }
    }

    private static class ByteArrayResponseHandler implements ResponseHandler<byte[]> {
        private ByteArrayResponseHandler() {
        }

        public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            return response.getStatusLine().getStatusCode() == 200 ? "200".getBytes() : null;
        }
    }
}

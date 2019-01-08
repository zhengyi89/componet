//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.http;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.client.model.Call;
import com.zbjdl.common.utils.event.client.model.CodeEnum;
import com.zbjdl.common.utils.event.client.model.DocumentCall;
import com.zbjdl.common.utils.event.client.model.MapDocumentCall;
import com.zbjdl.common.utils.event.client.model.RpcCall;
import com.zbjdl.common.utils.event.client.model.StringDocumentCall;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.impl.spi.ProtocolInvoker;
import com.zbjdl.common.utils.event.lang.diagnostic.Profiler;
import com.zbjdl.common.utils.event.lang.uri.WebURI;
import com.zbjdl.common.utils.event.utils.HessianSerUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInvoker implements ProtocolInvoker {
    private static final Logger logger = LoggerFactory.getLogger(HttpInvoker.class);
    private HttpInvoker.ByteArrayResponseHandler byteArrayResponseHandler = new HttpInvoker.ByteArrayResponseHandler();
    private HttpClientProxy httpClientProxy = null;
    private static final String PROTOCOL_HTTP = "http";
    private static final String PROTOCOL_HTTPS = "https";

    public HttpInvoker() {
    }

    public boolean supportProtocol(String eventName) {
        return eventName.startsWith("http") || eventName.startsWith("https");
    }

    public Object handle(SokulaEvent event) {
        if (event.getDomain() instanceof Call) {
            Call<?> call = (Call)event.getDomain();
            WebURI serviceURI = call.getServiceUri();
            Profiler.enter("HttpInvoker.handle");

            Exception var5;
            try {
                Object var4 = this.invoker(serviceURI, call);
                return var4;
            } catch (Exception var9) {
                var5 = var9;
            } finally {
                Profiler.release();
            }

            return var5;
        } else {
            throw new RuntimeException("event domain is not a Call object. Domain = " + event.getDomain());
        }
    }

    public byte[] handle(String uri, String content) throws Exception {
        ByteArrayEntity entity = new ByteArrayEntity(content.getBytes("UTF-8"));
        HttpPost httppost = new HttpPost(uri);
        httppost.setEntity(entity);
        return (byte[])this.httpClientProxy.execute(httppost, this.byteArrayResponseHandler);
    }

    private Object invoker(WebURI serviceUri, Call<?> call) throws Exception {
        if (call.isRemote()) {
            Object obj = null;
            if (call instanceof RpcCall) {
                obj = this.rpcCall(serviceUri, (RpcCall)call);
            } else {
                obj = this.documentCall(serviceUri, (DocumentCall)call);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("HttpInvoker : uri = " + serviceUri.getRawPath() + " | call = " + call + " | return = " + obj);
            }

            return obj;
        } else {
            throw new RuntimeException("Don't support locall invoke . call.isRemote = " + call.isRemote());
        }
    }

    private Object rpcCall(WebURI serviceUri, RpcCall call) throws Exception {
        if (call.getCode() == CodeEnum.HESSIEN.code()) {
            return this.remoteHessienInvoke(serviceUri, (Object[])call.getParameter());
        } else {
            throw new RuntimeException("Don't support code format . call.getCode = " + call.getCode());
        }
    }

    private Object documentCall(WebURI serviceUri, DocumentCall<?> call) throws Exception {
        if (call instanceof StringDocumentCall) {
            StringDocumentCall sCall = (StringDocumentCall)call;
            return StringUtils.isBlank((String)sCall.getParameter()) ? this.remoteGetReader(serviceUri, call.getEncoding()) : this.remotePostReader(serviceUri, (String)sCall.getParameter(), sCall.getEncoding());
        } else if (call instanceof MapDocumentCall) {
            MapDocumentCall mCall = (MapDocumentCall)call;
            return null != mCall.getParameter() && !((Map)mCall.getParameter()).isEmpty() ? this.remotePostReader(serviceUri, (Map)mCall.getParameter(), mCall.getEncoding()) : this.remoteGetReader(serviceUri, mCall.getEncoding());
        } else {
            throw new RuntimeException("Don't support call Object  . call = " + call.getClass().getName());
        }
    }

    private Object remoteHessienInvoke(WebURI webURI, Object[] params) throws Exception {
        try {
            byte[] content = HessianSerUtils.serialize(params);
            byte[] result = this.remotePostReader(webURI, content);
            return null != result && result.length >= 1 ? HessianSerUtils.deserialize(result) : null;
        } catch (Exception var5) {
            logger.error("remoteHessienInvoke Error , webURI : " + webURI.getRawPath() + " | params : " + Arrays.toString(params), var5);
            throw var5;
        }
    }

    private String remoteGetReader(WebURI webURI, String charset) throws Exception {
        HttpGet httpget = new HttpGet(this.httpClientProxy.getRawPath(webURI));
        httpget.setHeader(new BasicHeader("http.protocol.content-charset", charset));
        return new String((byte[])this.httpClientProxy.execute(httpget, this.byteArrayResponseHandler), charset);
    }

    private byte[] remotePostReader(WebURI webURI, byte[] content) throws Exception {
        ByteArrayEntity entity = new ByteArrayEntity(content);
        HttpPost httppost = new HttpPost(this.httpClientProxy.getRawPath(webURI));
        httppost.setEntity(entity);
        return (byte[])this.httpClientProxy.execute(httppost, this.byteArrayResponseHandler);
    }

    private String remotePostReader(WebURI webURI, String content, String charset) throws Exception {
        StringEntity entity = new StringEntity(content, charset);
        HttpPost httppost = new HttpPost(this.httpClientProxy.getRawPath(webURI));
        httppost.setEntity(entity);
        httppost.setHeader(new BasicHeader("http.protocol.content-charset", charset));
        return new String((byte[])this.httpClientProxy.execute(httppost, this.byteArrayResponseHandler), charset);
    }

    private byte[] remotePostReader(WebURI webURI, Map<String, Object> parameter, String charset) throws Exception {
        List<NameValuePair> nvps = new ArrayList();
        Iterator<String> keys = parameter.keySet().iterator();
        String key = null;
        Object value = null;

        while(keys.hasNext()) {
            key = (String)keys.next();
            value = parameter.get(key);
            if (null != value) {
                nvps.add(new BasicNameValuePair(key, value.toString()));
            }
        }

        return this.remotePostReaderProxy(webURI, nvps, charset, 0);
    }

    private byte[] remotePostReaderProxy(WebURI webURI, List<NameValuePair> nvps, String charset, int retryTimes) throws Exception {
        String rawPath = null;

        try {
            rawPath = this.httpClientProxy.getRawPath(webURI);
            HttpPost httppost = new HttpPost(rawPath);
            httppost.setEntity(new UrlEncodedFormEntity(nvps, charset));
            return (byte[])this.httpClientProxy.execute(httppost, this.byteArrayResponseHandler);
        } catch (EventRuntimeException var7) {
            if (var7.getErrorCode() == 503 && retryTimes < 3) {
                TimeUnit.MILLISECONDS.wait(100L);
                return this.remotePostReaderProxy(webURI, nvps, charset, retryTimes++);
            } else {
                var7.setMessage(var7.getMessage() + ",rawpath" + rawPath);
                throw var7;
            }
        }
    }

    public void setHttpClientProxy(HttpClientProxy httpClientProxy) {
        this.httpClientProxy = httpClientProxy;
    }

    private static class ByteArrayResponseHandler implements ResponseHandler<byte[]> {
        private ByteArrayResponseHandler() {
        }

        public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            int code = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            if (code == 200) {
                entity = response.getEntity();
                return entity != null ? EntityUtils.toByteArray(entity) : null;
            } else if (code == 999) {
                throw new EventRuntimeException(999, "不支持的远程调用协议，请使用 xxx.hessian");
            } else if (code == 998) {
                entity = response.getEntity();
                String msg = null;
                if (entity != null) {
                    msg = EntityUtils.toString(entity);
                }

                throw new EventRuntimeException(998, msg);
            } else {
                throw new EventRuntimeException(code, "reason : " + response.getStatusLine().getReasonPhrase());
            }
        }
    }
}

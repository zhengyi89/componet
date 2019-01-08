//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.vm;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.client.model.Call;
import com.zbjdl.common.utils.event.impl.spi.ProtocolInvoker;
import com.zbjdl.common.utils.event.impl.spi.http.HttpInvoker;
import com.zbjdl.common.utils.event.lang.invoker.SpringInvokerUtils;
import com.zbjdl.common.utils.event.lang.uri.WebURI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringVmInvoker implements ProtocolInvoker {
    private static final Logger logger = LoggerFactory.getLogger(SpringVmInvoker.class);
    public static final String PROTOCOL = "spring";
    public static final String ACTION_NAME = "actionName";
    private HttpInvoker httpInvoker;

    public SpringVmInvoker() {
    }

    public void init() {
    }

    public void destroy() {
    }

    public boolean supportProtocol(String eventName) {
        return eventName.startsWith("spring");
    }

    public Object handle(SokulaEvent event) {
        if (event.getDomain() instanceof Call) {
            Call<?> call = (Call)event.getDomain();
            WebURI serviceURI = call.getServiceUri();
            String reqPath = serviceURI.getRequestPath();
            int p = reqPath.lastIndexOf("/");
            String serviceName = reqPath.substring(p + 1);
            p = serviceName.indexOf(".");
            if (p > 0) {
                serviceName = serviceName.substring(0, p);
            }

            if (logger.isInfoEnabled()) {
                logger.info("invokeEvent: " + event);
            }

            if (!SpringInvokerUtils.hasLocalService(serviceName, event.getAppName())) {
                if (StringUtils.isNotBlank(serviceURI.getHost())) {
                    if (logger.isInfoEnabled()) {
                        logger.info("spring remote invoke for " + serviceName + ": " + serviceURI.getRawPath());
                    }

                    call.setRemote(true);
                    return this.httpInvoker.handle(event);
                } else {
                    throw new RuntimeException("event has not local service and isn't valide remote service. Domain = " + call + " | serviceURI.host = " + serviceURI.getHost());
                }
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("spring local invoke for " + serviceName + ": " + serviceURI.getRawPath());
                }

                Object[] parameters = (Object[])((Object[])call.getParameter());
                String actionName = (String)serviceURI.getParameter("actionName");

                try {
                    Object obj = SpringInvokerUtils.invoker(serviceName, actionName, parameters);
                    return obj;
                } catch (Exception var11) {
                    Throwable root = ExceptionUtils.getRootCause(var11);
                    logger.error("sping call has error, domain=" + call + "\n message=" + root.getMessage());
                    if (root instanceof RuntimeException) {
                        throw (RuntimeException)root;
                    } else if (var11 instanceof RuntimeException) {
                        throw (RuntimeException)var11;
                    } else {
                        throw new RuntimeException("sping call has error, domain=" + call, var11);
                    }
                }
            }
        } else {
            throw new RuntimeException("event domain is not a Call object. Domain = " + event.getDomain());
        }
    }

    public void setHttpInvoker(HttpInvoker httpInvoker) {
        this.httpInvoker = httpInvoker;
    }
}

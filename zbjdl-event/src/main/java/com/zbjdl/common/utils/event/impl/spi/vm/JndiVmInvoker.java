//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.vm;

import com.zbjdl.common.utils.event.EventListener;
import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.client.model.Call;
import com.zbjdl.common.utils.event.client.model.RpcCall;
import com.zbjdl.common.utils.event.impl.spi.ProtocolInvoker;
import com.zbjdl.common.utils.event.lang.jndi.ObjectBindContext;
import com.zbjdl.common.utils.event.lang.uri.WebURI;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MethodInvoker;

public class JndiVmInvoker implements ProtocolInvoker {
    private static final Logger logger = LoggerFactory.getLogger(JndiVmInvoker.class);
    public static final String PROTOCOL = "jndivm";
    public static final String ACTION_NAME = "actionName";
    private static Map<String, MethodInvoker> contextCache = new HashMap();

    public JndiVmInvoker() {
    }

    public void init() {
    }

    public void destroy() {
        contextCache.clear();
    }

    public boolean supportProtocol(String eventName) {
        return eventName.startsWith("jndivm");
    }

    public Object handle(SokulaEvent event) {
        if (event.getDomain() instanceof Call) {
            Call<?> call = (Call)event.getDomain();
            WebURI serviceURI = call.getServiceUri();

            try {
                return this.invoker(serviceURI, call);
            } catch (Exception var5) {
                return var5;
            }
        } else {
            throw new RuntimeException("event domain is not a Call object. Domain = " + event.getDomain());
        }
    }

    private Object invoker(WebURI serviceUri, Call<?> call) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("JndiVmInvoker :  URI =  " + serviceUri.getRawPath() + " | call = " + call);
        }

        String rawPath = serviceUri.getRawPath();
        String servicePath = rawPath;
        int p = rawPath.indexOf("?");
        if (p > 0) {
            servicePath = rawPath.substring(0, p);
        }

        Object provider = ObjectBindContext.lookup(serviceUri);
        MethodInvoker methodInvoker = null;
        Object[] parameters = (Object[])((Object[])call.getParameter());
        String methodName;
        if (provider instanceof EventListener) {
            methodName = this.buildJndiName(servicePath, "handle");
            methodInvoker = (MethodInvoker)contextCache.get(methodName);
            if (null == methodInvoker) {
                methodInvoker = new MethodInvoker();
                methodInvoker.setTargetClass(provider.getClass());
                methodInvoker.setTargetMethod("handle");
                methodInvoker.setTargetObject(provider);
                methodInvoker.setArguments(parameters);
                methodInvoker.prepare();
                contextCache.put(methodName, methodInvoker);
            }
        } else {
            methodName = (String)serviceUri.getParameter("actionName");
            if (!StringUtils.isNotBlank(methodName)) {
                throw new RuntimeException("Don't specify 'actionName' by uri . URI: " + rawPath);
            }

            String jndiUri = this.buildJndiName(servicePath, methodName);
            methodInvoker = (MethodInvoker)contextCache.get(jndiUri);
            if (null == methodInvoker) {
                methodInvoker = new MethodInvoker();
                methodInvoker.setTargetClass(provider.getClass());
                methodInvoker.setTargetMethod(methodName);
                methodInvoker.setTargetObject(provider);
                methodInvoker.setArguments(parameters);
                methodInvoker.prepare();
                contextCache.put(jndiUri, methodInvoker);
            }
        }

        if (call instanceof RpcCall) {
            methodInvoker.setArguments(parameters);
            return methodInvoker.invoke();
        } else {
            throw new UnsupportedOperationException("Don't support.");
        }
    }

    private String buildJndiName(String path, String methodName) {
        return "mi:" + path + "/" + methodName;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.invoker;

import com.zbjdl.common.utils.event.lang.diagnostic.Profiler;
import org.springframework.util.MethodInvoker;

public class ClassInvokerUtils {
    public ClassInvokerUtils() {
    }

    public static Object invoker(String servicePath, Object provider, String actionName, Object[] parameters) throws Exception {
        Profiler.enter("ClassInvokerUtils.invoker");

        Object var5;
        try {
            MethodInvoker methodInvoker = new MethodInvoker();
            methodInvoker.setTargetClass(provider.getClass());
            methodInvoker.setTargetMethod(actionName);
            methodInvoker.setTargetObject(provider);
            methodInvoker.setArguments(parameters);
            methodInvoker.prepare();
            var5 = methodInvoker.invoke();
        } finally {
            Profiler.release();
        }

        return var5;
    }
}

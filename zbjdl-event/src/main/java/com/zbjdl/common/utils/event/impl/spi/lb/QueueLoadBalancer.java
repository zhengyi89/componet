//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.lb;

import com.zbjdl.common.utils.event.SokulaEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class QueueLoadBalancer extends LoadBalancerSupport {
    protected static final Logger logger = LoggerFactory.getLogger(QueueLoadBalancer.class);

    QueueLoadBalancer() {
    }

    public Processor getProcessor(SokulaEvent event) {
        List<Processor> list = this.getProcessors();
        return !list.isEmpty() ? this.chooseProcessor(list, event) : null;
    }

    protected abstract Processor chooseProcessor(List<Processor> var1, SokulaEvent var2);
}

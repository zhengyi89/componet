//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.lb;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class LoadBalancerSupport implements LoadBalancer {
    private final List<Processor> processors = new CopyOnWriteArrayList();

    public LoadBalancerSupport() {
    }

    public void addProcessor(Processor processor) {
        if (!this.processors.contains(processor)) {
            this.processors.add(processor);
        }

    }

    public void removeProcessor(Processor processor) {
        this.processors.remove(processor);
    }

    public List<Processor> getProcessors() {
        return this.processors;
    }
}

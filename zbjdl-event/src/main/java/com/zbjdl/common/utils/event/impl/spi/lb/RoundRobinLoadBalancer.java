//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.lb;

import com.zbjdl.common.utils.event.SokulaEvent;
import java.util.List;

public class RoundRobinLoadBalancer extends QueueLoadBalancer {
    private int counter = -1;

    public RoundRobinLoadBalancer() {
    }

    protected synchronized Processor chooseProcessor(List<Processor> processors, SokulaEvent event) {
        int size = processors.size();
        if (++this.counter >= size) {
            this.counter = 0;
        }

        return (Processor)processors.get(this.counter);
    }

    public String toString() {
        return "RoundRobinLoadBalancer";
    }
}

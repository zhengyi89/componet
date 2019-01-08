//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.spi.lb;

import com.zbjdl.common.utils.event.SokulaEvent;
import java.util.List;

public class RandomLoadBalancer extends QueueLoadBalancer {
    public RandomLoadBalancer() {
    }

    protected synchronized Processor chooseProcessor(List<Processor> processors, SokulaEvent event) {
        int size = processors.size();

        int index;
        do {
            index = (int)Math.round(Math.random() * (double)size);
        } while(index >= size);

        return (Processor)processors.get(index);
    }

    public String toString() {
        return "RandomLoadBalancer";
    }
}

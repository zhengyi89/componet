//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.runtime;

import java.util.Stack;

public final class ProductContextHolder {
    private static ThreadLocal<ProductContextHolder.ProductContextCounter> context = new ThreadLocal();

    public ProductContextHolder() {
    }

    public static void end() {
        ProductContextHolder.ProductContextCounter counter = (ProductContextHolder.ProductContextCounter)context.get();
        if (null != counter) {
            ProductContext productContext = counter.pop();
            if (null != productContext) {
                productContext.end();
            }

            if (counter.contextStack.isEmpty()) {
                context.set(null);
            }
        }

    }

    public static ProductContext begin() {
        if (null == context.get()) {
            ProductContextHolder.ProductContextCounter counter = new ProductContextHolder.ProductContextCounter();
            counter.init();
            context.set(counter);
        } else {
            ((ProductContextHolder.ProductContextCounter)context.get()).repush();
        }

        ProductContext productContext = ((ProductContextHolder.ProductContextCounter)context.get()).peek();
        productContext.begin();
        return productContext;
    }

    public static ProductContext begin(ProductContext productContext) {
        if (null != productContext) {
            ProductContextHolder.ProductContextCounter counter = (ProductContextHolder.ProductContextCounter)context.get();
            if (null == counter) {
                counter = new ProductContextHolder.ProductContextCounter();
                context.set(counter);
            }

            counter.push(productContext);
            productContext.begin();
            return productContext;
        } else {
            return begin();
        }
    }

    public static ProductContext getProductContext() {
        ProductContextHolder.ProductContextCounter counter = (ProductContextHolder.ProductContextCounter)context.get();
        return null != counter ? counter.peek() : null;
    }

    private static class ProductContextCounter {
        public Stack<ProductContext> contextStack = new Stack();

        public ProductContextCounter() {
        }

        public void init() {
            this.contextStack.push(new ProductContext());
        }

        public void push(ProductContext context) {
            this.contextStack.push(context);
        }

        public void repush() {
            this.contextStack.push(this.contextStack.peek());
        }

        public ProductContext peek() {
            return this.contextStack.isEmpty() ? null : (ProductContext)this.contextStack.peek();
        }

        public ProductContext pop() {
            return (ProductContext)this.contextStack.pop();
        }
    }
}

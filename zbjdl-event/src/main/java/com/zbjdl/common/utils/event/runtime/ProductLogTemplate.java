//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.runtime;

public class ProductLogTemplate {
    public ProductLogTemplate() {
    }

    public Object execute(ProductContext productContext, ProductLogCallBack action) throws Throwable {
        if (null == productContext) {
            return action.execute();
        } else {
            long t1 = System.currentTimeMillis();
            ProductLog oldPlog = null;
            ProductLog plog = new ProductLog();
            boolean var14 = false;

            Object var7;
            try {
                var14 = true;
                oldPlog = productContext.getProductLog();
                productContext.initProductLog(plog);
                ProductContextHolder.begin(productContext);
                var7 = action.execute();
                var14 = false;
            } finally {
                if (var14) {
                    long t2 = System.currentTimeMillis();
                    bizLogger(productContext, plog, t2 - t1);
                    ProductContextHolder.end();
                    productContext.initProductLog(oldPlog);
                }
            }

            long t2 = System.currentTimeMillis();
            bizLogger(productContext, plog, t2 - t1);
            ProductContextHolder.end();
            productContext.initProductLog(oldPlog);
            return var7;
        }
    }

    private static final void bizLogger(ProductContext productContext, ProductLog plog, long times) {
        if (null != plog) {
            plog.log(productContext, times);
        }

    }
}

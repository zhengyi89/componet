//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    String name() default "";

    String cacheRegion() default "";

    /** @deprecated */
    @Deprecated
    String usage() default "store";

    CacheTypeEnum type() default CacheTypeEnum.LOCAL;

    LocalCache local() default @LocalCache(
    timeToIdleSeconds = -1,
    timeToLiveSeconds = -1
);

    RemoteCache remote() default @RemoteCache;
}

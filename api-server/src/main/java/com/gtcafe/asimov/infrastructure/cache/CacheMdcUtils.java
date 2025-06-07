package com.gtcafe.asimov.infrastructure.cache;

import java.time.Duration;

import org.jboss.logging.MDC;

public final class CacheMdcUtils {
    
    public static void UpdateWrite_Delete(String cacheKey) {
        MDC.put("CacheKey", cacheKey);
        // MDC.put("CacheValue", value);
        MDC.put("CacheAction", "write");
        MDC.put("CacheMethod", "delete");

    }

    public static void UpdateRead_Get(String cacheKey) {
        MDC.put("CacheKey", cacheKey);
        MDC.put("CacheAction", "read");
        MDC.put("CacheMethod", "get");
    }

    public static void UpdateWrite_Set(String cacheKey, Duration timeout) {
        MDC.put("CacheKey", cacheKey);
        // MDC.put("CacheValue", value);
        MDC.put("CacheAction", "write");
        MDC.put("CacheMethod", "set");
        MDC.put("CacheTimeout", timeout);
    }

    public static void UpdateWrite_Put(String cacheKey, long timeout) {
        MDC.put("CacheKey", cacheKey);
        // MDC.put("CacheValue", value);
        MDC.put("CacheAction", "write");
        MDC.put("CacheMethod", "put_update");
        MDC.put("CacheTimeout", timeout);
    }
}

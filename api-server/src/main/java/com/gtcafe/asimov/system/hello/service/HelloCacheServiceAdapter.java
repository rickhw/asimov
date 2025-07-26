package com.gtcafe.asimov.system.hello.service;

import org.springframework.context.annotation.Configuration;

import com.gtcafe.asimov.system.hello.service.HelloCacheService;
import com.gtcafe.asimov.system.hello.service.HelloCacheService.CacheMetricsRecorder;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * API-Server 端的 HelloCacheService 適配器
 * 負責整合 API-Server 特定的指標服務
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class HelloCacheServiceAdapter {

    private final HelloCacheService cacheService;
    private final HelloMetricsService metricsService;

    @PostConstruct
    public void initialize() {
        // 設定指標記錄器
        cacheService.setMetricsRecorder(new ApiServerCacheMetricsRecorder());
        log.info("HelloCacheService adapter initialized for API-Server");
    }

    /**
     * API-Server 端的快取指標記錄器
     */
    private class ApiServerCacheMetricsRecorder implements CacheMetricsRecorder {
        @Override
        public void recordCacheOperation(String operation, boolean success) {
            // 這裡可以整合 HelloMetricsService 的指標記錄
            // 目前 HelloMetricsService 沒有快取相關的方法，所以先記錄 log
            log.debug("API-Server cache operation: {} - {}", operation, success ? "success" : "failure");
            
            // 未來可以在 HelloMetricsService 中添加快取指標方法，例如：
            // metricsService.recordCacheOperation(operation, success);
        }
    }
}
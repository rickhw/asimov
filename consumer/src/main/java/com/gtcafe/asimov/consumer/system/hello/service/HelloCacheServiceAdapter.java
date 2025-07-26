package com.gtcafe.asimov.consumer.system.hello.service;

import org.springframework.context.annotation.Configuration;

import com.gtcafe.asimov.system.hello.service.HelloCacheService;
import com.gtcafe.asimov.system.hello.service.HelloCacheService.CacheMetricsRecorder;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Consumer 端的 HelloCacheService 適配器
 * 負責整合 Consumer 特定的指標服務
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class HelloCacheServiceAdapter {

    private final HelloCacheService cacheService;
    private final HelloConsumerMetricsService metricsService;

    @PostConstruct
    public void initialize() {
        // 設定指標記錄器
        cacheService.setMetricsRecorder(new ConsumerCacheMetricsRecorder());
        log.info("HelloCacheService adapter initialized for Consumer");
    }

    /**
     * Consumer 端的快取指標記錄器
     */
    private class ConsumerCacheMetricsRecorder implements CacheMetricsRecorder {
        @Override
        public void recordCacheOperation(String operation, boolean success) {
            // 整合 HelloConsumerMetricsService 的指標記錄
            metricsService.recordCacheOperation(operation, success);
        }
    }
}
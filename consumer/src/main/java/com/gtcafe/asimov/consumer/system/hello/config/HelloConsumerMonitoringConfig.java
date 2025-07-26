package com.gtcafe.asimov.consumer.system.hello.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.gtcafe.asimov.consumer.system.hello.service.HelloConsumerMetricsService;
import com.gtcafe.asimov.consumer.system.hello.service.HelloCacheInvalidationHandler;
import com.gtcafe.asimov.system.hello.service.HelloCacheService;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello Consumer 監控配置
 * 負責設定監控相關的排程任務和配置
 */
@Configuration
@Slf4j
public class HelloConsumerMonitoringConfig {

    @Autowired
    private HelloConsumerMetricsService metricsService;
    
    @Autowired
    private HelloCacheService cacheService;
    
    @Autowired
    private HelloCacheInvalidationHandler invalidationHandler;
    
    private ScheduledExecutorService scheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Initializing Hello Consumer monitoring...");
        
        // 註冊快取失效監聽器
        cacheService.registerInvalidationListener(invalidationHandler);
        log.info("Registered cache invalidation handler");
        
        // 初始化排程器
        scheduler = Executors.newScheduledThreadPool(2);
        
        // 定期記錄指標摘要
        scheduler.scheduleAtFixedRate(
            this::logMetricsSummary,
            1, // 初始延遲 1 分鐘
            5, // 每 5 分鐘執行一次
            TimeUnit.MINUTES
        );
        
        // 定期計算吞吐量
        scheduler.scheduleAtFixedRate(
            this::calculateThroughput,
            30, // 初始延遲 30 秒
            30, // 每 30 秒執行一次
            TimeUnit.SECONDS
        );
        
        log.info("Hello Consumer monitoring initialized successfully");
    }

    /**
     * 記錄指標摘要
     */
    private void logMetricsSummary() {
        try {
            metricsService.logMetricsSummary();
        } catch (Exception e) {
            log.error("Failed to log metrics summary", e);
        }
    }

    /**
     * 計算吞吐量
     */
    private void calculateThroughput() {
        try {
            // 這裡可以實作吞吐量計算邏輯
            // 目前簡單設定為 0，實際應該根據處理的訊息數量計算
            metricsService.recordThroughput(0.0);
        } catch (Exception e) {
            log.error("Failed to calculate throughput", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            log.info("Shutting down Hello Consumer monitoring scheduler...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            log.info("Hello Consumer monitoring scheduler shut down completed");
        }
    }
}
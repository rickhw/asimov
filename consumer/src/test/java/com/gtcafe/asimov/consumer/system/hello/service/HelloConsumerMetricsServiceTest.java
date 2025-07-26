package com.gtcafe.asimov.consumer.system.hello.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

/**
 * HelloConsumerMetricsService 單元測試
 * 使用 SimpleMeterRegistry 進行簡單的功能測試
 */
@ExtendWith(MockitoExtension.class)
class HelloConsumerMetricsServiceTest {

    @Test
    void testMetricsServiceInitialization() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        assertNotNull(metricsService);
    }

    @Test
    void testRecordMessageProcessed() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 測試方法執行不會拋出異常
        metricsService.recordMessageProcessed();
    }

    @Test
    void testRecordMessageFailed() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 測試方法執行不會拋出異常
        metricsService.recordMessageFailed("test_error");
    }

    @Test
    void testStartAndRecordMessageProcessingTime() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 測試計時器功能
        var sample = metricsService.startMessageProcessingTimer();
        assertNotNull(sample);
        
        // 模擬一些處理時間
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 測試方法執行不會拋出異常
        metricsService.recordMessageProcessingTime(sample);
    }

    @Test
    void testRecordDlqMessage() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 測試方法執行不會拋出異常
        metricsService.recordDlqMessage("timeout");
    }

    @Test
    void testRecordTaskStateTransition() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 測試方法執行不會拋出異常
        metricsService.recordTaskStateTransition("PENDING", "RUNNING");
    }

    @Test
    void testRecordMessageSize() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 測試方法執行不會拋出異常
        metricsService.recordMessageSize(1024);
    }

    @Test
    void testRecordProcessingDelay() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        Duration delay = Duration.ofMillis(500);
        // 測試方法執行不會拋出異常
        metricsService.recordProcessingDelay(delay);
    }

    @Test
    void testRecordEndToEndProcessingTime() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        Duration totalTime = Duration.ofSeconds(2);
        String traceId = "test-trace-123";
        
        // 測試方法執行不會拋出異常
        metricsService.recordEndToEndProcessingTime(totalTime, traceId);
    }

    @Test
    void testRecordHealthCheck() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 測試方法執行不會拋出異常
        metricsService.recordHealthCheck("cache", true);
    }

    @Test
    void testRecordThroughput() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 測試方法執行不會拋出異常
        metricsService.recordThroughput(10.5);
    }

    @Test
    void testLogMetricsSummary() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        HelloConsumerMetricsService metricsService = new HelloConsumerMetricsService(meterRegistry);
        
        // 先記錄一些指標
        metricsService.recordMessageProcessed();
        metricsService.recordMessageFailed("test_error");
        
        // 測試方法執行不會拋出異常
        metricsService.logMetricsSummary();
    }
}
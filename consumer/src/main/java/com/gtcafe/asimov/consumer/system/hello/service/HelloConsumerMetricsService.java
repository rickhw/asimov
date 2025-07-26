package com.gtcafe.asimov.consumer.system.hello.service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello Consumer 指標監控服務
 * 負責收集和記錄 Consumer 端的業務指標
 */
@Service
@Slf4j
public class HelloConsumerMetricsService {

    private final MeterRegistry meterRegistry;
    
    // 計數器指標
    private final Counter messagesProcessedCounter;
    private final Counter messagesFailedCounter;
    private final Counter dlqMessagesCounter;
    private final Counter retryAttemptsCounter;
    private final Counter taskStateTransitionsCounter;
    
    // 計時器指標
    private final Timer messageProcessingTimer;
    private final Timer businessLogicTimer;
    private final Timer cacheOperationTimer;
    
    // 分布摘要指標
    private final DistributionSummary messageSizeSummary;
    private final DistributionSummary processingDelaySummary;
    
    // 儀表指標
    private final AtomicLong activeProcessingMessages = new AtomicLong(0);
    private final AtomicLong totalDlqMessages = new AtomicLong(0);
    private final AtomicLong lastProcessingTime = new AtomicLong(0);

    public HelloConsumerMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // 初始化計數器
        this.messagesProcessedCounter = Counter.builder("hello_consumer_messages_processed_total")
                .description("Total number of messages processed by hello consumer")
                .register(meterRegistry);
                
        this.messagesFailedCounter = Counter.builder("hello_consumer_messages_failed_total")
                .description("Total number of messages that failed processing")
                .register(meterRegistry);
                
        this.dlqMessagesCounter = Counter.builder("hello_consumer_dlq_messages_total")
                .description("Total number of messages sent to dead letter queue")
                .register(meterRegistry);
                
        this.retryAttemptsCounter = Counter.builder("hello_consumer_retry_attempts_total")
                .description("Total number of retry attempts")
                .register(meterRegistry);
                
        this.taskStateTransitionsCounter = Counter.builder("hello_consumer_task_state_transitions_total")
                .description("Total number of task state transitions")
                .register(meterRegistry);
        
        // 初始化計時器
        this.messageProcessingTimer = Timer.builder("hello_consumer_message_processing_duration_seconds")
                .description("Time taken to process a message")
                .register(meterRegistry);
                
        this.businessLogicTimer = Timer.builder("hello_consumer_business_logic_duration_seconds")
                .description("Time taken for business logic processing")
                .register(meterRegistry);
                
        this.cacheOperationTimer = Timer.builder("hello_consumer_cache_operation_duration_seconds")
                .description("Time taken for cache operations")
                .register(meterRegistry);
        
        // 初始化分布摘要
        this.messageSizeSummary = DistributionSummary.builder("hello_consumer_message_size_bytes")
                .description("Distribution of message sizes")
                .register(meterRegistry);
                
        this.processingDelaySummary = DistributionSummary.builder("hello_consumer_processing_delay_seconds")
                .description("Distribution of processing delays")
                .register(meterRegistry);
        
        // 初始化儀表
        Gauge.builder("hello_consumer_active_processing_messages", this, HelloConsumerMetricsService::getActiveProcessingMessages)
                .description("Number of messages currently being processed")
                .register(meterRegistry);
                
        Gauge.builder("hello_consumer_total_dlq_messages", this, HelloConsumerMetricsService::getTotalDlqMessages)
                .description("Total number of messages in dead letter queue")
                .register(meterRegistry);
                
        Gauge.builder("hello_consumer_last_processing_time_seconds", this, HelloConsumerMetricsService::getLastProcessingTime)
                .description("Timestamp of last message processing")
                .register(meterRegistry);
        
        log.info("HelloConsumerMetricsService initialized with all metrics registered");
    }

    // 訊息處理指標
    public void recordMessageProcessed() {
        messagesProcessedCounter.increment();
        log.debug("Recorded message processed metric");
    }

    public void recordMessageFailed(String errorType) {
        Counter.builder("hello_consumer_messages_failed_total")
                .description("Total number of messages that failed processing by error type")
                .tag("error_type", errorType)
                .register(meterRegistry)
                .increment();
        messagesFailedCounter.increment();
        log.debug("Recorded message failed metric for error type: {}", errorType);
    }

    public Timer.Sample startMessageProcessingTimer() {
        activeProcessingMessages.incrementAndGet();
        return Timer.start(meterRegistry);
    }

    public void recordMessageProcessingTime(Timer.Sample sample) {
        sample.stop(messageProcessingTimer);
        activeProcessingMessages.decrementAndGet();
        lastProcessingTime.set(System.currentTimeMillis() / 1000);
        log.debug("Recorded message processing time metric");
    }

    // 業務邏輯指標
    public Timer.Sample startBusinessLogicTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordBusinessLogicTime(Timer.Sample sample) {
        sample.stop(businessLogicTimer);
        log.debug("Recorded business logic time metric");
    }

    // DLQ 指標
    public void recordDlqMessage(String reason) {
        Counter.builder("hello_consumer_dlq_messages_total")
                .description("Total number of messages sent to DLQ by reason")
                .tag("reason", reason)
                .register(meterRegistry)
                .increment();
        dlqMessagesCounter.increment();
        totalDlqMessages.incrementAndGet();
        log.debug("Recorded DLQ message metric for reason: {}", reason);
    }

    // 重試指標
    public void recordRetryAttempt(String retryReason) {
        Counter.builder("hello_consumer_retry_attempts_total")
                .description("Total number of retry attempts by reason")
                .tag("reason", retryReason)
                .register(meterRegistry)
                .increment();
        retryAttemptsCounter.increment();
        log.debug("Recorded retry attempt metric for reason: {}", retryReason);
    }

    // 任務狀態轉換指標
    public void recordTaskStateTransition(String fromState, String toState) {
        Counter.builder("hello_consumer_task_state_transitions_total")
                .description("Total number of task state transitions")
                .tag("from_state", fromState)
                .tag("to_state", toState)
                .register(meterRegistry)
                .increment();
        taskStateTransitionsCounter.increment();
        log.debug("Recorded task state transition metric: {} -> {}", fromState, toState);
    }

    // 快取操作指標
    public Timer.Sample startCacheOperationTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordCacheOperationTime(Timer.Sample sample) {
        sample.stop(cacheOperationTimer);
        log.debug("Recorded cache operation time metric");
    }

    // 訊息大小指標
    public void recordMessageSize(int sizeBytes) {
        messageSizeSummary.record(sizeBytes);
        log.debug("Recorded message size metric: {} bytes", sizeBytes);
    }

    // 處理延遲指標
    public void recordProcessingDelay(Duration delay) {
        processingDelaySummary.record(delay.toMillis());
        log.debug("Recorded processing delay metric: {}ms", delay.toMillis());
    }

    // 端到端追蹤指標
    public void recordEndToEndProcessingTime(Duration totalTime, String traceId) {
        Timer.builder("hello_consumer_end_to_end_duration_seconds")
                .description("End-to-end processing time from producer to consumer")
                .tag("trace_id", traceId != null ? traceId : "unknown")
                .register(meterRegistry)
                .record(totalTime);
        log.debug("Recorded end-to-end processing time metric: {}ms, traceId: {}", 
            totalTime.toMillis(), traceId);
    }

    // 健康檢查指標
    public void recordHealthCheck(String component, boolean healthy) {
        // 使用 AtomicReference 來儲存健康狀態
        java.util.concurrent.atomic.AtomicReference<Double> healthStatus = 
            new java.util.concurrent.atomic.AtomicReference<>(healthy ? 1.0 : 0.0);
        
        Gauge.builder("hello_consumer_health_status", healthStatus, ref -> ref.get())
                .description("Health status of consumer components")
                .tag("component", component)
                .register(meterRegistry);
        log.debug("Recorded health check metric for {}: {}", component, healthy);
    }

    // 效能指標
    public void recordThroughput(double messagesPerSecond) {
        // 使用 AtomicReference 來儲存吞吐量
        java.util.concurrent.atomic.AtomicReference<Double> throughput = 
            new java.util.concurrent.atomic.AtomicReference<>(messagesPerSecond);
            
        Gauge.builder("hello_consumer_throughput_messages_per_second", throughput, ref -> ref.get())
                .description("Current message processing throughput")
                .register(meterRegistry);
        log.debug("Recorded throughput metric: {} messages/second", messagesPerSecond);
    }

    // Getter methods for Gauge metrics
    private double getActiveProcessingMessages() {
        return activeProcessingMessages.get();
    }

    private double getTotalDlqMessages() {
        return totalDlqMessages.get();
    }

    private double getLastProcessingTime() {
        return lastProcessingTime.get();
    }

    // 統計方法
    public void logMetricsSummary() {
        log.info("Consumer Metrics Summary:");
        log.info("- Messages Processed: {}", messagesProcessedCounter.count());
        log.info("- Messages Failed: {}", messagesFailedCounter.count());
        log.info("- DLQ Messages: {}", dlqMessagesCounter.count());
        log.info("- Retry Attempts: {}", retryAttemptsCounter.count());
        log.info("- Active Processing: {}", activeProcessingMessages.get());
        log.info("- Average Processing Time: {}ms", 
            messageProcessingTimer.mean(java.util.concurrent.TimeUnit.MILLISECONDS));
    }
}
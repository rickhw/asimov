package com.gtcafe.asimov.consumer.system.hello.service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.gtcafe.asimov.system.hello.service.AbstractHelloMetricsService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello Consumer 指標監控服務
 * 負責收集和記錄 Consumer 端的業務指標
 * 繼承 AbstractHelloMetricsService 以共享通用功能
 */
@Service
@Slf4j
public class HelloConsumerMetricsService extends AbstractHelloMetricsService {
    
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
        super(meterRegistry);
        
        // 在建構子中直接初始化 final 變數
        this.messagesProcessedCounter = createCounter("hello_consumer_messages_processed_total", "Total number of messages processed by hello consumer");
        this.messagesFailedCounter = createCounter("hello_consumer_messages_failed_total", "Total number of messages that failed processing");
        this.dlqMessagesCounter = createCounter("hello_consumer_dlq_messages_total", "Total number of messages sent to dead letter queue");
        this.retryAttemptsCounter = createCounter("hello_consumer_retry_attempts_total", "Total number of retry attempts");
        this.taskStateTransitionsCounter = createCounter("hello_consumer_task_state_transitions_total", "Total number of task state transitions");
        
        this.messageProcessingTimer = createTimer("hello_consumer_message_processing_duration_seconds", "Time taken to process a message");
        this.businessLogicTimer = createTimer("hello_consumer_business_logic_duration_seconds", "Time taken for business logic processing");
        this.cacheOperationTimer = createTimer("hello_consumer_cache_operation_duration_seconds", "Time taken for cache operations");
        
        this.messageSizeSummary = createDistributionSummary("hello_consumer_message_size_bytes", "Distribution of message sizes");
        this.processingDelaySummary = createDistributionSummary("hello_consumer_processing_delay_seconds", "Distribution of processing delays");
        
        // 初始化儀表指標
        initializeSpecificMetrics();
        
        log.info("HelloConsumerMetricsService initialized with all metrics registered");
    }

    @Override
    protected String getMetricPrefix() {
        return "hello_consumer";
    }

    @Override
    protected void initializeSpecificMetrics() {
        // 初始化 Consumer 特有的儀表
        createGauge("hello_consumer_active_processing_messages", "Number of messages currently being processed", this, HelloConsumerMetricsService::getActiveProcessingMessages);
        createGauge("hello_consumer_total_dlq_messages", "Total number of messages in dead letter queue", this, HelloConsumerMetricsService::getTotalDlqMessages);
        createGauge("hello_consumer_last_processing_time_seconds", "Timestamp of last message processing", this, HelloConsumerMetricsService::getLastProcessingTime);
    }

    // 訊息處理指標
    public void recordMessageProcessed() {
        messagesProcessedCounter.increment();
        log.debug("Recorded message processed metric");
    }

    public void recordMessageFailed(String errorType) {
        recordTaggedCounter("hello_consumer_messages_failed_total", "Total number of messages that failed processing by error type", "error_type", errorType);
        messagesFailedCounter.increment();
        log.debug("Recorded message failed metric for error type: {}", errorType);
    }

    public Timer.Sample startMessageProcessingTimer() {
        activeProcessingMessages.incrementAndGet();
        return startTimer();
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

    public void recordCacheOperation(String operation, boolean success) {
        Counter.builder("hello_consumer_cache_operations_total")
                .description("Total number of cache operations")
                .tag("operation", operation)
                .tag("success", String.valueOf(success))
                .register(meterRegistry)
                .increment();
        log.debug("Recorded cache operation metric: {} - {}", operation, success ? "success" : "failure");
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
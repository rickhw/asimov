package com.gtcafe.asimov.system.hello.service;

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
 * Hello 指標監控服務
 * 負責收集和記錄 Hello 相關的業務指標
 */
@Service
@Slf4j
public class HelloMetricsService {

    private final MeterRegistry meterRegistry;

    // 計數器指標
    private final Counter syncRequestCounter;
    private final Counter asyncRequestCounter;
    private final Counter validationFailureCounter;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter databaseOperationCounter;
    private final Counter queueOperationCounter;

    // 計時器指標
    private final Timer syncProcessingTimer;
    private final Timer asyncProcessingTimer;
    private final Timer validationTimer;
    private final Timer cacheOperationTimer;
    private final Timer databaseOperationTimer;

    // 分布摘要指標
    private final DistributionSummary messageLengthSummary;
    private final DistributionSummary batchSizeSummary;

    // 儀表指標
    private final AtomicLong activeAsyncRequests = new AtomicLong(0);
    private final AtomicLong totalCachedEvents = new AtomicLong(0);

    public HelloMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // 初始化計數器
        this.syncRequestCounter = Counter.builder("hello.requests.sync")
                .description("Number of synchronous hello requests")
                .register(meterRegistry);

        this.asyncRequestCounter = Counter.builder("hello.requests.async")
                .description("Number of asynchronous hello requests")
                .register(meterRegistry);

        this.validationFailureCounter = Counter.builder("hello.validation.failures")
                .description("Number of validation failures")
                .tag("type", "all")
                .register(meterRegistry);

        this.cacheHitCounter = Counter.builder("hello.cache.hits")
                .description("Number of cache hits")
                .register(meterRegistry);

        this.cacheMissCounter = Counter.builder("hello.cache.misses")
                .description("Number of cache misses")
                .register(meterRegistry);

        this.databaseOperationCounter = Counter.builder("hello.database.operations")
                .description("Number of database operations")
                .register(meterRegistry);

        this.queueOperationCounter = Counter.builder("hello.queue.operations")
                .description("Number of queue operations")
                .register(meterRegistry);

        // 初始化計時器
        this.syncProcessingTimer = Timer.builder("hello.processing.sync")
                .description("Time taken to process synchronous hello requests")
                .register(meterRegistry);

        this.asyncProcessingTimer = Timer.builder("hello.processing.async")
                .description("Time taken to process asynchronous hello requests")
                .register(meterRegistry);

        this.validationTimer = Timer.builder("hello.validation.time")
                .description("Time taken for validation")
                .register(meterRegistry);

        this.cacheOperationTimer = Timer.builder("hello.cache.operation.time")
                .description("Time taken for cache operations")
                .register(meterRegistry);

        this.databaseOperationTimer = Timer.builder("hello.database.operation.time")
                .description("Time taken for database operations")
                .register(meterRegistry);

        // 初始化分布摘要
        this.messageLengthSummary = DistributionSummary.builder("hello.message.length")
                .description("Distribution of hello message lengths")
                .register(meterRegistry);

        this.batchSizeSummary = DistributionSummary.builder("hello.batch.size")
                .description("Distribution of batch sizes")
                .register(meterRegistry);

        // 初始化儀表
        Gauge.builder("hello.requests.active.async", this, HelloMetricsService::getActiveAsyncRequests)
                .description("Number of active asynchronous requests")
                .register(meterRegistry);

        Gauge.builder("hello.cache.events.total", this, HelloMetricsService::getTotalCachedEvents)
                .description("Total number of cached events")
                .register(meterRegistry);

        log.info("HelloMetricsService initialized with all metrics registered");
    }

    // 同步請求指標
    public void recordSyncRequest() {
        syncRequestCounter.increment();
        log.debug("Recorded sync request metric");
    }

    public Timer.Sample startSyncProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordSyncProcessingTime(Timer.Sample sample) {
        sample.stop(syncProcessingTimer);
        log.debug("Recorded sync processing time metric");
    }

    // 非同步請求指標
    public void recordAsyncRequest() {
        asyncRequestCounter.increment();
        activeAsyncRequests.incrementAndGet();
        log.debug("Recorded async request metric");
    }

    public Timer.Sample startAsyncProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordAsyncProcessingTime(Timer.Sample sample) {
        sample.stop(asyncProcessingTimer);
        activeAsyncRequests.decrementAndGet();
        log.debug("Recorded async processing time metric");
    }

    // 驗證指標
    public void recordValidationFailure(String validationType) {
        Counter.builder("hello.validation.failures")
                .description("Number of validation failures by type")
                .tag("type", validationType)
                .register(meterRegistry)
                .increment();
        
        validationFailureCounter.increment();
        log.debug("Recorded validation failure metric for type: {}", validationType);
    }

    public Timer.Sample startValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordValidationTime(Timer.Sample sample) {
        sample.stop(validationTimer);
        log.debug("Recorded validation time metric");
    }

    // 快取指標
    public void recordCacheHit() {
        cacheHitCounter.increment();
        log.debug("Recorded cache hit metric");
    }

    public void recordCacheMiss() {
        cacheMissCounter.increment();
        log.debug("Recorded cache miss metric");
    }

    public Timer.Sample startCacheOperationTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordCacheOperationTime(Timer.Sample sample) {
        sample.stop(cacheOperationTimer);
        log.debug("Recorded cache operation time metric");
    }

    public void recordCacheEventAdded() {
        totalCachedEvents.incrementAndGet();
        log.debug("Recorded cache event added metric");
    }

    public void recordCacheEventRemoved() {
        totalCachedEvents.decrementAndGet();
        log.debug("Recorded cache event removed metric");
    }

    // 資料庫指標
    public void recordDatabaseOperation(String operationType) {
        Counter.builder("hello.database.operations")
                .description("Number of database operations by type")
                .tag("type", operationType)
                .register(meterRegistry)
                .increment();
        
        databaseOperationCounter.increment();
        log.debug("Recorded database operation metric for type: {}", operationType);
    }

    public Timer.Sample startDatabaseOperationTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordDatabaseOperationTime(Timer.Sample sample) {
        sample.stop(databaseOperationTimer);
        log.debug("Recorded database operation time metric");
    }

    // 佇列指標
    public void recordQueueOperation(String operationType) {
        Counter.builder("hello.queue.operations")
                .description("Number of queue operations by type")
                .tag("type", operationType)
                .register(meterRegistry)
                .increment();
        
        queueOperationCounter.increment();
        log.debug("Recorded queue operation metric for type: {}", operationType);
    }

    // 訊息長度指標
    public void recordMessageLength(int length) {
        messageLengthSummary.record(length);
        log.debug("Recorded message length metric: {}", length);
    }

    // 批次大小指標
    public void recordBatchSize(int size) {
        batchSizeSummary.record(size);
        log.debug("Recorded batch size metric: {}", size);
    }

    // 自訂業務指標
    public void recordForbiddenWordDetection(String word) {
        Counter.builder("hello.security.forbidden.words")
                .description("Number of forbidden word detections")
                .tag("word", word)
                .register(meterRegistry)
                .increment();
        log.debug("Recorded forbidden word detection metric for: {}", word);
    }

    public void recordSecurityThreatDetection(String threatType) {
        Counter.builder("hello.security.threats")
                .description("Number of security threat detections")
                .tag("type", threatType)
                .register(meterRegistry)
                .increment();
        log.debug("Recorded security threat detection metric for type: {}", threatType);
    }

    // 效能指標
    public void recordProcessingLatency(Duration latency, String operationType) {
        Timer.builder("hello.processing.latency")
                .description("Processing latency by operation type")
                .tag("operation", operationType)
                .register(meterRegistry)
                .record(latency);
        log.debug("Recorded processing latency metric for {}: {}ms", operationType, latency.toMillis());
    }

    // Getter methods for Gauge metrics
    private double getActiveAsyncRequests() {
        return activeAsyncRequests.get();
    }

    private double getTotalCachedEvents() {
        return totalCachedEvents.get();
    }

    // 健康檢查指標
    public void recordHealthCheck(String component, boolean healthy) {
        double healthValue = healthy ? 1.0 : 0.0;
        Gauge.builder("hello.health.status", () -> healthValue)
                .description("Health status of components")
                .tag("component", component)
                .register(meterRegistry);
        log.debug("Recorded health check metric for {}: {}", component, healthy);
    }
}
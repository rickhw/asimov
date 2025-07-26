package com.gtcafe.asimov.system.hello.service;

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
 * Hello API-Server 指標監控服務
 * 負責收集和記錄 API-Server 端的業務指標
 * 繼承 AbstractHelloMetricsService 以共享通用功能
 */
@Service
@Slf4j
public class HelloMetricsService extends AbstractHelloMetricsService {

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
        super(meterRegistry);
        initializeSpecificMetrics();

        log.info("HelloMetricsService initialized with all metrics registered");
    }

    @Override
    protected String getMetricPrefix() {
        return "hello";
    }

    @Override
    protected void initializeSpecificMetrics() {
        // 初始化 API-Server 特有的計數器
        this.syncRequestCounter = createCounter("hello.requests.sync", "Number of synchronous hello requests");
        this.asyncRequestCounter = createCounter("hello.requests.async", "Number of asynchronous hello requests");
        this.validationFailureCounter = createTaggedCounter("hello.validation.failures", "Number of validation failures", "type", "all");
        this.cacheHitCounter = createCounter("hello.cache.hits", "Number of cache hits");
        this.cacheMissCounter = createCounter("hello.cache.misses", "Number of cache misses");
        this.databaseOperationCounter = createCounter("hello.database.operations", "Number of database operations");
        this.queueOperationCounter = createCounter("hello.queue.operations", "Number of queue operations");

        // 初始化 API-Server 特有的計時器
        this.syncProcessingTimer = createTimer("hello.processing.sync", "Time taken to process synchronous hello requests");
        this.asyncProcessingTimer = createTimer("hello.processing.async", "Time taken to process asynchronous hello requests");
        this.validationTimer = createTimer("hello.validation.time", "Time taken for validation");
        this.cacheOperationTimer = createTimer("hello.cache.operation.time", "Time taken for cache operations");
        this.databaseOperationTimer = createTimer("hello.database.operation.time", "Time taken for database operations");

        // 初始化 API-Server 特有的分布摘要
        this.messageLengthSummary = createDistributionSummary("hello.message.length", "Distribution of hello message lengths");
        this.batchSizeSummary = createDistributionSummary("hello.batch.size", "Distribution of batch sizes");

        // 初始化 API-Server 特有的儀表
        createGauge("hello.requests.active.async", "Number of active asynchronous requests", this, HelloMetricsService::getActiveAsyncRequests);
        createGauge("hello.cache.events.total", "Total number of cached events", this, HelloMetricsService::getTotalCachedEvents);
    }

    // 同步請求指標
    public void recordSyncRequest() {
        syncRequestCounter.increment();
        log.debug("Recorded sync request metric");
    }

    public Timer.Sample startSyncProcessingTimer() {
        return startTimer();
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
        return startTimer();
    }

    public void recordAsyncProcessingTime(Timer.Sample sample) {
        sample.stop(asyncProcessingTimer);
        activeAsyncRequests.decrementAndGet();
        log.debug("Recorded async processing time metric");
    }

    // 驗證指標
    public void recordValidationFailure(String validationType) {
        recordTaggedCounter("hello.validation.failures", "Number of validation failures by type", "type", validationType);
        validationFailureCounter.increment();
        log.debug("Recorded validation failure metric for type: {}", validationType);
    }

    public Timer.Sample startValidationTimer() {
        return startTimer();
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
        return startTimer();
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
        recordTaggedCounter("hello.database.operations", "Number of database operations by type", "type", operationType);
        databaseOperationCounter.increment();
        log.debug("Recorded database operation metric for type: {}", operationType);
    }

    public Timer.Sample startDatabaseOperationTimer() {
        return startTimer();
    }

    public void recordDatabaseOperationTime(Timer.Sample sample) {
        sample.stop(databaseOperationTimer);
        log.debug("Recorded database operation time metric");
    }

    // 佇列指標
    public void recordQueueOperation(String operationType) {
        recordTaggedCounter("hello.queue.operations", "Number of queue operations by type", "type", operationType);
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
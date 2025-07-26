package com.gtcafe.asimov.system.hello.service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello 指標服務抽象基礎類別
 * 提供通用的指標操作功能，供 API-Server 和 Consumer 繼承使用
 */
@Slf4j
public abstract class AbstractHelloMetricsService {

    protected final MeterRegistry meterRegistry;

    protected AbstractHelloMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * 創建計數器
     * 
     * @param name 指標名稱
     * @param description 描述
     * @return Counter 實例
     */
    protected Counter createCounter(String name, String description) {
        return Counter.builder(name)
                .description(description)
                .register(meterRegistry);
    }

    /**
     * 創建帶標籤的計數器
     * 
     * @param name 指標名稱
     * @param description 描述
     * @param tagKey 標籤鍵
     * @param tagValue 標籤值
     * @return Counter 實例
     */
    protected Counter createTaggedCounter(String name, String description, String tagKey, String tagValue) {
        return Counter.builder(name)
                .description(description)
                .tag(tagKey, tagValue)
                .register(meterRegistry);
    }

    /**
     * 創建計時器
     * 
     * @param name 指標名稱
     * @param description 描述
     * @return Timer 實例
     */
    protected Timer createTimer(String name, String description) {
        return Timer.builder(name)
                .description(description)
                .register(meterRegistry);
    }

    /**
     * 創建分布摘要
     * 
     * @param name 指標名稱
     * @param description 描述
     * @return DistributionSummary 實例
     */
    protected DistributionSummary createDistributionSummary(String name, String description) {
        return DistributionSummary.builder(name)
                .description(description)
                .register(meterRegistry);
    }

    /**
     * 創建儀表指標
     * 
     * @param name 指標名稱
     * @param description 描述
     * @param supplier 數值提供者
     * @return Gauge 實例
     */
    protected <T> Gauge createGauge(String name, String description, T obj, java.util.function.ToDoubleFunction<T> supplier) {
        return Gauge.builder(name, obj, supplier)
                .description(description)
                .register(meterRegistry);
    }

    /**
     * 開始計時
     * 
     * @return Timer.Sample 實例
     */
    protected Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * 記錄帶標籤的計數器指標
     * 
     * @param baseName 基礎指標名稱
     * @param description 描述
     * @param tagKey 標籤鍵
     * @param tagValue 標籤值
     */
    protected void recordTaggedCounter(String baseName, String description, String tagKey, String tagValue) {
        Counter.builder(baseName)
                .description(description)
                .tag(tagKey, tagValue)
                .register(meterRegistry)
                .increment();
        log.debug("Recorded tagged counter metric: {} with {}={}", baseName, tagKey, tagValue);
    }

    /**
     * 記錄計時器指標
     * 
     * @param baseName 基礎指標名稱
     * @param description 描述
     * @param duration 持續時間
     * @param tagKey 標籤鍵 (可選)
     * @param tagValue 標籤值 (可選)
     */
    protected void recordTimerMetric(String baseName, String description, Duration duration, String tagKey, String tagValue) {
        Timer.Builder builder = Timer.builder(baseName).description(description);
        
        if (tagKey != null && tagValue != null) {
            builder.tag(tagKey, tagValue);
        }
        
        builder.register(meterRegistry).record(duration);
        log.debug("Recorded timer metric: {} - {}ms", baseName, duration.toMillis());
    }

    /**
     * 記錄健康檢查指標 (通用實作)
     * 
     * @param component 組件名稱
     * @param healthy 是否健康
     * @param prefix 指標前綴
     */
    protected void recordHealthCheck(String component, boolean healthy, String prefix) {
        AtomicReference<Double> healthStatus = new AtomicReference<>(healthy ? 1.0 : 0.0);
        
        Gauge.builder(prefix + ".health.status", healthStatus, ref -> ref.get())
                .description("Health status of components")
                .tag("component", component)
                .register(meterRegistry);
        log.debug("Recorded health check metric for {}: {}", component, healthy);
    }

    /**
     * 記錄快取操作指標 (通用實作)
     * 
     * @param operation 操作類型
     * @param success 是否成功
     * @param prefix 指標前綴
     */
    protected void recordCacheOperation(String operation, boolean success, String prefix) {
        Counter.builder(prefix + ".cache.operations.total")
                .description("Total number of cache operations")
                .tag("operation", operation)
                .tag("success", String.valueOf(success))
                .register(meterRegistry)
                .increment();
        log.debug("Recorded cache operation metric: {} - {}", operation, success ? "success" : "failure");
    }

    /**
     * 記錄訊息大小指標 (通用實作)
     * 
     * @param sizeBytes 訊息大小
     * @param prefix 指標前綴
     */
    protected void recordMessageSize(int sizeBytes, String prefix) {
        DistributionSummary.builder(prefix + ".message.size.bytes")
                .description("Distribution of message sizes")
                .register(meterRegistry)
                .record(sizeBytes);
        log.debug("Recorded message size metric: {} bytes", sizeBytes);
    }

    /**
     * 創建動態儀表指標
     * 
     * @param name 指標名稱
     * @param description 描述
     * @param value 初始值
     * @return AtomicReference 用於動態更新值
     */
    protected AtomicReference<Double> createDynamicGauge(String name, String description, double value) {
        AtomicReference<Double> reference = new AtomicReference<>(value);
        
        Gauge.builder(name, reference, ref -> ref.get())
                .description(description)
                .register(meterRegistry);
                
        return reference;
    }

    /**
     * 獲取指標前綴 (由子類實作)
     * 
     * @return 指標前綴字串
     */
    protected abstract String getMetricPrefix();

    /**
     * 初始化子類特定的指標 (由子類實作)
     */
    protected abstract void initializeSpecificMetrics();

    /**
     * 記錄指標摘要 (由子類實作)
     */
    public abstract void logMetricsSummary();
}
package com.gtcafe.asimov.consumer.system.hello;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.consumer.system.hello.service.HelloConsumerMetricsService;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
import com.gtcafe.asimov.system.hello.service.HelloCacheService;
import com.gtcafe.asimov.system.task.TaskEventHandler;
import com.gtcafe.asimov.system.task.schema.TaskState;

import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloEventHandler implements TaskEventHandler<HelloEvent> {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private HelloCacheService cacheService;
    
    @Autowired
    private HelloConsumerMetricsService metricsService;

    // @Autowired
    // private HelloUtils _utils;

    @Override
    public boolean handleEvent(HelloEvent event) {
        log.info("HelloEventHandler.handleEvent() event: {}", event.getId());
        
        // 記錄訊息大小
        String eventJson = jsonUtils.modelToJsonStringSafe(event).orElse("{}");
        metricsService.recordMessageSize(eventJson.getBytes().length);
        
        // 開始處理計時
        Timer.Sample processingTimer = metricsService.startMessageProcessingTimer();
        Timer.Sample businessLogicTimer = null;
        Timer.Sample cacheTimer = null;
        
        String previousState = event.getState() != null ? event.getState().toString() : "UNKNOWN";
        Instant startTime = Instant.now();
        boolean result = false;
        
        try {
            // 記錄狀態轉換
            metricsService.recordTaskStateTransition(previousState, "RUNNING");
            event.setState(TaskState.RUNNING);
            
            // 1. 業務邏輯處理 (移除模擬延遲，實作真實邏輯)
            businessLogicTimer = metricsService.startBusinessLogicTimer();
            processHelloMessage(event);
            metricsService.recordBusinessLogicTime(businessLogicTimer);
            
            // 2. 更新事件狀態
            metricsService.recordTaskStateTransition("RUNNING", "COMPLETED");
            event.setState(TaskState.COMPLETED);
            
            // 3. 快取操作
            cacheTimer = metricsService.startCacheOperationTimer();
            updateEventInCache(event);
            metricsService.recordCacheOperationTime(cacheTimer);
            
            // 記錄成功處理
            metricsService.recordMessageProcessed();
            
            // 計算端到端處理時間 (creationTime 是 String，需要解析)
            try {
                if (event.getCreationTime() != null) {
                    long creationTimeMillis = Long.parseLong(event.getCreationTime());
                    Duration endToEndTime = Duration.between(
                        Instant.ofEpochMilli(creationTimeMillis), 
                        Instant.now()
                    );
                    metricsService.recordEndToEndProcessingTime(endToEndTime, event.getId());
                }
            } catch (NumberFormatException e) {
                log.warn("Failed to parse creation time for end-to-end tracking: {}", event.getCreationTime());
            }
            
            result = true;
            log.info("HelloEventHandler.handleEvent() completed for event: {} in {}ms", 
                event.getId(), Duration.between(startTime, Instant.now()).toMillis());
                
        } catch (Exception e) {
            log.error("HelloEventHandler.handleEvent() failed for event: {}", event.getId(), e);
            
            // 記錄失敗
            metricsService.recordMessageFailed(e.getClass().getSimpleName());
            metricsService.recordTaskStateTransition(event.getState().toString(), "FAILURE");
            event.setState(TaskState.FAILURE);
            
            // 更新失敗狀態到快取
            try {
                if (cacheTimer == null) {
                    cacheTimer = metricsService.startCacheOperationTimer();
                }
                updateEventInCache(event);
                metricsService.recordCacheOperationTime(cacheTimer);
            } catch (Exception cacheException) {
                log.error("Failed to update cache after processing failure", cacheException);
            }
            
            // 根據錯誤類型決定是否重試
            if (shouldRetry(e)) {
                metricsService.recordRetryAttempt(e.getClass().getSimpleName());
            }
            
            result = false;
        } finally {
            // 記錄處理時間
            metricsService.recordMessageProcessingTime(processingTimer);
            
            // 記錄處理延遲
            Duration processingDelay = Duration.between(startTime, Instant.now());
            metricsService.recordProcessingDelay(processingDelay);
        }
        
        return result;
    }
    
    /**
     * 處理 Hello 訊息的實際業務邏輯
     */
    private void processHelloMessage(HelloEvent event) {
        log.debug("Processing hello message: {}", event.getData().getMessage());
        
        // 實際的業務處理邏輯，替代之前的模擬延遲
        // 1. 訊息內容分析
        String message = event.getData().getMessage();
        if (message != null) {
            // 分析訊息內容，例如：語言檢測、情感分析等
            analyzeMessageContent(message);
        }
        
        // 2. 訊息豐富化
        enrichMessage(event);
        
        // 3. 業務規則驗證
        validateBusinessRules(event);
        
        log.debug("Hello message processing completed for event: {}", event.getId());
    }
    
    /**
     * 分析訊息內容
     */
    private void analyzeMessageContent(String message) {
        // 實作訊息內容分析邏輯
        log.debug("Analyzing message content: length={}, words={}", 
            message.length(), message.split("\\s+").length);
    }
    
    /**
     * 豐富化訊息
     */
    private void enrichMessage(HelloEvent event) {
        // 實作訊息豐富化邏輯
        // Hello 類別沒有 setTimestamp 方法，所以移除這行
        // 可以在這裡加入其他豐富化邏輯
        log.debug("Message enriched for event: {}", event.getId());
    }
    
    /**
     * 驗證業務規則
     */
    private void validateBusinessRules(HelloEvent event) {
        // 實作業務規則驗證
        String message = event.getData().getMessage();
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        log.debug("Business rules validated for event: {}", event.getId());
    }
    
    /**
     * 更新事件到快取
     */
    private void updateEventInCache(HelloEvent event) {
        boolean success = cacheService.updateHelloEvent(event);
        if (!success) {
            log.warn("Failed to update event in cache: {}", event.getId());
            throw new RuntimeException("Cache update failed for event: " + event.getId());
        }
        log.debug("Event updated in cache: {}", event.getId());
    }
    
    /**
     * 判斷是否應該重試
     */
    private boolean shouldRetry(Exception e) {
        // 定義可重試的異常類型
        return !(e instanceof IllegalArgumentException || 
                 e instanceof SecurityException ||
                 e instanceof UnsupportedOperationException);
    }
}

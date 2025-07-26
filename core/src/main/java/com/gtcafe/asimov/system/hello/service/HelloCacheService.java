package com.gtcafe.asimov.system.hello.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.hello.config.HelloCacheConfig;
import com.gtcafe.asimov.system.hello.model.HelloEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello 統一快取服務
 * 負責處理 Hello 相關的快取操作，包括儲存、檢索、更新和刪除
 * 統一供 API-Server 和 Consumer 使用
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HelloCacheService {

    private final CacheRepository cacheRepository;
    private final JsonUtils jsonUtils;
    private final HelloCacheConfig cacheConfig;

    // 快取配置常數
    private static final String LOCK_SUFFIX = ":lock";
    private static final String INVALIDATION_CHANNEL = "hello:cache:invalidation";
    
    // 快取失效監聽器列表
    private final List<CacheInvalidationListener> invalidationListeners = new CopyOnWriteArrayList<>();
    
    // 可選的指標服務 (由各模組自行注入)
    private CacheMetricsRecorder metricsRecorder;

    /**
     * 設定指標記錄器 (可選)
     * 
     * @param metricsRecorder 指標記錄器
     */
    public void setMetricsRecorder(CacheMetricsRecorder metricsRecorder) {
        this.metricsRecorder = metricsRecorder;
        log.debug("Cache metrics recorder set: {}", metricsRecorder.getClass().getSimpleName());
    }

    /**
     * 儲存 HelloEvent 到快取
     * 同時儲存到主要快取和任務索引快取
     * 
     * @param event HelloEvent 物件
     * @return 是否儲存成功
     */
    public boolean cacheHelloEvent(HelloEvent event) {
        log.debug("Caching hello event with ID: {}", event.getId());
        
        if (!cacheConfig.isEnabled()) {
            log.debug("Cache is disabled, skipping cache operation for event: {}", event.getId());
            return true;
        }
        
        try {
            String eventJsonString = jsonUtils.modelToJsonStringSafe(event)
                .orElseThrow(() -> new RuntimeException("Failed to serialize hello event to JSON"));

            // 儲存到主要快取
            String primaryKey = generatePrimaryKey(event.getId());
            cacheRepository.saveOrUpdateObject(primaryKey, eventJsonString, 
                cacheConfig.getPrimaryTtl().toSeconds(), TimeUnit.SECONDS);

            // 儲存到任務索引快取
            String taskIndexKey = generateTaskIndexKey(event.getId());
            cacheRepository.saveOrUpdateObject(taskIndexKey, eventJsonString, 
                cacheConfig.getTaskIndexTtl().toSeconds(), TimeUnit.SECONDS);

            log.debug("Successfully cached hello event with keys: {} and {}", 
                primaryKey, taskIndexKey);
            
            // 記錄快取操作指標
            recordMetrics("save", true);
            
            // 通知快取更新
            notifyCacheInvalidation(event.getId(), CacheInvalidationType.UPDATE);
            
            return true;

        } catch (Exception e) {
            log.error("Failed to cache hello event with ID: {}", event.getId(), e);
            
            // 記錄快取操作失敗指標
            recordMetrics("save", false);
            
            if (cacheConfig.isFailOnCacheError()) {
                throw new RuntimeException("Cache operation failed", e);
            }
            return false;
        }
    }

    /**
     * 從快取檢索 HelloEvent
     * 
     * @param eventId 事件 ID
     * @return HelloEvent 物件，如果不存在則返回 empty
     */
    public Optional<HelloEvent> getHelloEvent(String eventId) {
        log.debug("Retrieving hello event from cache with ID: {}", eventId);
        
        if (!cacheConfig.isEnabled()) {
            log.debug("Cache is disabled, returning empty for event: {}", eventId);
            return Optional.empty();
        }
        
        try {
            String primaryKey = generatePrimaryKey(eventId);
            Optional<String> cachedValue = cacheRepository.retrieveObject(primaryKey);
            
            if (cachedValue.isEmpty()) {
                log.debug("Cache miss for hello event ID: {}", eventId);
                recordMetrics("get", false);
                return Optional.empty();
            }

            Optional<HelloEvent> event = jsonUtils.jsonStringToModelSafe(
                cachedValue.get(), HelloEvent.class);
            
            if (event.isPresent()) {
                log.debug("Cache hit for hello event ID: {}", eventId);
                recordMetrics("get", true);
            } else {
                log.warn("Failed to deserialize cached hello event with ID: {}", eventId);
                recordMetrics("get", false);
            }
            
            return event;

        } catch (Exception e) {
            log.error("Error retrieving hello event from cache with ID: {}", eventId, e);
            recordMetrics("get", false);
            return Optional.empty();
        }
    }

    /**
     * 從任務索引快取檢索 HelloEvent
     * 
     * @param eventId 事件 ID
     * @return HelloEvent 物件，如果不存在則返回 empty
     */
    public Optional<HelloEvent> getHelloEventFromTaskIndex(String eventId) {
        log.debug("Retrieving hello event from task index cache with ID: {}", eventId);
        
        if (!cacheConfig.isEnabled()) {
            log.debug("Cache is disabled, returning empty for event: {}", eventId);
            return Optional.empty();
        }
        
        try {
            String taskIndexKey = generateTaskIndexKey(eventId);
            Optional<String> cachedValue = cacheRepository.retrieveObject(taskIndexKey);
            
            if (cachedValue.isEmpty()) {
                log.debug("Task index cache miss for hello event ID: {}", eventId);
                return Optional.empty();
            }

            Optional<HelloEvent> event = jsonUtils.jsonStringToModelSafe(
                cachedValue.get(), HelloEvent.class);
            
            if (event.isPresent()) {
                log.debug("Task index cache hit for hello event ID: {}", eventId);
            } else {
                log.warn("Failed to deserialize cached hello event from task index with ID: {}", eventId);
            }
            
            return event;

        } catch (Exception e) {
            log.error("Error retrieving hello event from task index cache with ID: {}", eventId, e);
            return Optional.empty();
        }
    }

    /**
     * 更新快取中的 HelloEvent
     * 
     * @param event 更新後的 HelloEvent 物件
     * @return 是否更新成功
     */
    public boolean updateHelloEvent(HelloEvent event) {
        log.debug("Updating hello event in cache with ID: {}", event.getId());
        return cacheHelloEvent(event); // 重用儲存邏輯
    }

    /**
     * 從快取中刪除 HelloEvent
     * 
     * @param eventId 事件 ID
     * @return 是否刪除成功
     */
    public boolean deleteHelloEvent(String eventId) {
        log.debug("Deleting hello event from cache with ID: {}", eventId);
        
        if (!cacheConfig.isEnabled()) {
            log.debug("Cache is disabled, skipping delete operation for event: {}", eventId);
            return true;
        }
        
        try {
            String primaryKey = generatePrimaryKey(eventId);
            String taskIndexKey = generateTaskIndexKey(eventId);
            
            // 刪除主要快取
            cacheRepository.delete(primaryKey);
            
            // 刪除任務索引快取
            cacheRepository.delete(taskIndexKey);
            
            log.debug("Successfully deleted hello event from cache with ID: {}", eventId);
            
            // 記錄快取操作指標
            recordMetrics("delete", true);
            
            // 通知快取刪除
            notifyCacheInvalidation(eventId, CacheInvalidationType.DELETE);
            
            return true;

        } catch (Exception e) {
            log.error("Failed to delete hello event from cache with ID: {}", eventId, e);
            
            // 記錄快取操作失敗指標
            recordMetrics("delete", false);
            
            if (cacheConfig.isFailOnCacheError()) {
                throw new RuntimeException("Cache delete operation failed", e);
            }
            return false;
        }
    }

    /**
     * 檢查快取中是否存在指定的 HelloEvent
     * 
     * @param eventId 事件 ID
     * @return 是否存在
     */
    public boolean existsInCache(String eventId) {
        if (!cacheConfig.isEnabled()) {
            return false;
        }
        
        try {
            String primaryKey = generatePrimaryKey(eventId);
            return cacheRepository.retrieveObject(primaryKey).isPresent();
        } catch (Exception e) {
            log.error("Error checking cache existence for hello event ID: {}", eventId, e);
            return false;
        }
    }

    /**
     * 使用分散式鎖執行快取操作
     * 
     * @param eventId 事件 ID
     * @param operation 要執行的操作
     * @return 操作結果
     */
    public <T> Optional<T> executeWithLock(String eventId, CacheOperation<T> operation) {
        if (!cacheConfig.isEnabled()) {
            try {
                return Optional.of(operation.execute());
            } catch (Exception e) {
                log.error("Error executing operation without cache lock for event ID: {}", eventId, e);
                return Optional.empty();
            }
        }
        
        String lockKey = generatePrimaryKey(eventId) + LOCK_SUFFIX;
        boolean lockAcquired = false;
        
        try {
            lockAcquired = cacheRepository.setIfNotExists(lockKey, 
                getLockValue(), cacheConfig.getLockTimeout());
            
            if (!lockAcquired) {
                log.warn("Failed to acquire cache lock for hello event ID: {}", eventId);
                return Optional.empty();
            }
            
            log.debug("Acquired cache lock for hello event ID: {}", eventId);
            return Optional.of(operation.execute());
            
        } catch (Exception e) {
            log.error("Error executing cache operation with lock for hello event ID: {}", 
                eventId, e);
            return Optional.empty();
        } finally {
            if (lockAcquired) {
                cacheRepository.delete(lockKey);
                log.debug("Released cache lock for hello event ID: {}", eventId);
            }
        }
    }

    /**
     * 清除所有 Hello 相關的快取
     * 謹慎使用，通常只在維護或測試時使用
     */
    public void clearAllHelloCache() {
        log.warn("Clearing all hello cache entries");
        
        try {
            // 清除主要快取
            var primaryKeys = cacheRepository.findKeysByPattern(
                KindConstants.PLATFORM_HELLO + ":*");
            primaryKeys.forEach(cacheRepository::delete);
            
            // 清除任務索引快取
            var taskIndexKeys = cacheRepository.findKeysByPattern(
                KindConstants.SYS_TASK + ":*");
            taskIndexKeys.forEach(cacheRepository::delete);
            
            log.info("Successfully cleared {} primary cache entries and {} task index entries", 
                primaryKeys.size(), taskIndexKeys.size());
                
        } catch (Exception e) {
            log.error("Error clearing hello cache", e);
        }
    }

    /**
     * 註冊快取失效監聽器
     * 
     * @param listener 監聽器
     */
    public void registerInvalidationListener(CacheInvalidationListener listener) {
        invalidationListeners.add(listener);
        log.debug("Registered cache invalidation listener: {}", listener.getClass().getSimpleName());
    }

    /**
     * 移除快取失效監聽器
     * 
     * @param listener 監聽器
     */
    public void unregisterInvalidationListener(CacheInvalidationListener listener) {
        invalidationListeners.remove(listener);
        log.debug("Unregistered cache invalidation listener: {}", listener.getClass().getSimpleName());
    }

    /**
     * 記錄指標 (如果有設定指標記錄器)
     */
    private void recordMetrics(String operation, boolean success) {
        if (cacheConfig.isMetricsEnabled() && metricsRecorder != null) {
            metricsRecorder.recordCacheOperation(operation, success);
        }
    }

    /**
     * 通知快取失效
     * 
     * @param eventId 事件 ID
     * @param operation 操作類型 (UPDATE, DELETE)
     */
    private void notifyCacheInvalidation(String eventId, CacheInvalidationType operation) {
        if (invalidationListeners.isEmpty()) {
            return;
        }
        
        log.debug("Notifying cache invalidation for event: {} with operation: {}", eventId, operation);
        
        CacheInvalidationEvent invalidationEvent = new CacheInvalidationEvent(
            eventId, operation, System.currentTimeMillis());
        
        // 通知所有監聽器
        for (CacheInvalidationListener listener : invalidationListeners) {
            try {
                listener.onCacheInvalidation(invalidationEvent);
            } catch (Exception e) {
                log.error("Error notifying cache invalidation listener: {}", 
                    listener.getClass().getSimpleName(), e);
            }
        }
        
        // 發布到 Redis 頻道 (如果需要跨服務通知)
        publishInvalidationToChannel(invalidationEvent);
    }

    /**
     * 發布快取失效事件到 Redis 頻道
     * 
     * @param event 失效事件
     */
    private void publishInvalidationToChannel(CacheInvalidationEvent event) {
        try {
            String eventJson = jsonUtils.modelToJsonStringSafe(event)
                .orElseThrow(() -> new RuntimeException("Failed to serialize invalidation event"));
            
            // 將失效事件儲存到特殊的快取鍵中，供其他服務查詢
            String invalidationKey = String.format("%s:invalidation:%s:%d", 
                INVALIDATION_CHANNEL, event.getEventId(), event.getTimestamp());
            
            cacheRepository.saveOrUpdateObject(invalidationKey, eventJson, 
                Duration.ofMinutes(5).toSeconds(), TimeUnit.SECONDS);
            
            log.debug("Stored cache invalidation event with key: {}", invalidationKey);
        } catch (Exception e) {
            log.error("Failed to store cache invalidation event", e);
        }
    }

    /**
     * 生成主要快取鍵
     */
    private String generatePrimaryKey(String eventId) {
        return String.format("%s:%s", KindConstants.PLATFORM_HELLO, eventId);
    }

    /**
     * 生成任務索引快取鍵
     */
    private String generateTaskIndexKey(String eventId) {
        return String.format("%s:%s", KindConstants.SYS_TASK, eventId);
    }

    /**
     * 生成鎖值
     */
    private String getLockValue() {
        return String.format("%d-%d", Thread.currentThread().getId(), System.currentTimeMillis());
    }

    /**
     * 快取操作介面
     */
    @FunctionalInterface
    public interface CacheOperation<T> {
        T execute() throws Exception;
    }

    /**
     * 快取失效監聽器介面
     */
    @FunctionalInterface
    public interface CacheInvalidationListener {
        void onCacheInvalidation(CacheInvalidationEvent event);
    }

    /**
     * 快取指標記錄器介面
     * 各模組可實作此介面來記錄快取操作指標
     */
    public interface CacheMetricsRecorder {
        void recordCacheOperation(String operation, boolean success);
    }

    /**
     * 快取失效事件
     */
    public static class CacheInvalidationEvent {
        private final String eventId;
        private final CacheInvalidationType operation;
        private final long timestamp;

        public CacheInvalidationEvent(String eventId, CacheInvalidationType operation, long timestamp) {
            this.eventId = eventId;
            this.operation = operation;
            this.timestamp = timestamp;
        }

        public String getEventId() { return eventId; }
        public CacheInvalidationType getOperation() { return operation; }
        public long getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return String.format("CacheInvalidationEvent{eventId='%s', operation=%s, timestamp=%d}", 
                eventId, operation, timestamp);
        }
    }

    /**
     * 快取失效操作類型
     */
    public enum CacheInvalidationType {
        UPDATE, DELETE, EXPIRE
    }
}
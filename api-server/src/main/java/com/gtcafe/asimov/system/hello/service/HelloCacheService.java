package com.gtcafe.asimov.system.hello.service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.hello.model.HelloEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello 快取服務
 * 負責處理 Hello 相關的快取操作，包括儲存、檢索、更新和刪除
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HelloCacheService {

    private final CacheRepository cacheRepository;
    private final JsonUtils jsonUtils;

    // 快取配置常數
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);
    private static final Duration TASK_INDEX_TTL = Duration.ofHours(24);
    private static final String LOCK_SUFFIX = ":lock";
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(10);

    /**
     * 儲存 HelloEvent 到快取
     * 同時儲存到主要快取和任務索引快取
     * 
     * @param event HelloEvent 物件
     * @return 是否儲存成功
     */
    public boolean cacheHelloEvent(HelloEvent event) {
        log.debug("Caching hello event with ID: {}", event.getId());
        
        try {
            String eventJsonString = jsonUtils.modelToJsonStringSafe(event)
                .orElseThrow(() -> new RuntimeException("Failed to serialize hello event to JSON"));

            // 儲存到主要快取
            String primaryKey = generatePrimaryKey(event.getId());
            cacheRepository.saveOrUpdateObject(primaryKey, eventJsonString, 
                DEFAULT_TTL.toSeconds(), TimeUnit.SECONDS);

            // 儲存到任務索引快取
            String taskIndexKey = generateTaskIndexKey(event.getId());
            cacheRepository.saveOrUpdateObject(taskIndexKey, eventJsonString, 
                TASK_INDEX_TTL.toSeconds(), TimeUnit.SECONDS);

            log.debug("Successfully cached hello event with keys: {} and {}", 
                primaryKey, taskIndexKey);
            return true;

        } catch (Exception e) {
            log.error("Failed to cache hello event with ID: {}", event.getId(), e);
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
        
        try {
            String primaryKey = generatePrimaryKey(eventId);
            Optional<String> cachedValue = cacheRepository.retrieveObject(primaryKey);
            
            if (cachedValue.isEmpty()) {
                log.debug("Cache miss for hello event ID: {}", eventId);
                return Optional.empty();
            }

            Optional<HelloEvent> event = jsonUtils.jsonStringToModelSafe(
                cachedValue.get(), HelloEvent.class);
            
            if (event.isPresent()) {
                log.debug("Cache hit for hello event ID: {}", eventId);
            } else {
                log.warn("Failed to deserialize cached hello event with ID: {}", eventId);
            }
            
            return event;

        } catch (Exception e) {
            log.error("Error retrieving hello event from cache with ID: {}", eventId, e);
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
        
        try {
            String primaryKey = generatePrimaryKey(eventId);
            String taskIndexKey = generateTaskIndexKey(eventId);
            
            // 刪除主要快取
            cacheRepository.delete(primaryKey);
            
            // 刪除任務索引快取
            cacheRepository.delete(taskIndexKey);
            
            log.debug("Successfully deleted hello event from cache with ID: {}", eventId);
            return true;

        } catch (Exception e) {
            log.error("Failed to delete hello event from cache with ID: {}", eventId, e);
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
        String lockKey = generatePrimaryKey(eventId) + LOCK_SUFFIX;
        boolean lockAcquired = false;
        
        try {
            lockAcquired = cacheRepository.setIfNotExists(lockKey, 
                getLockValue(), LOCK_TIMEOUT);
            
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
}
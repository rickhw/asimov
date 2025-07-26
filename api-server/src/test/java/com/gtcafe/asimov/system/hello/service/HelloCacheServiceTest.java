package com.gtcafe.asimov.system.hello.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.hello.config.HelloCacheConfig;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;

/**
 * HelloCacheService 單元測試
 * 測試快取服務的各種操作情境
 */
@ExtendWith(MockitoExtension.class)
class HelloCacheServiceTest {

    @Mock
    private CacheRepository cacheRepository;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private HelloCacheConfig cacheConfig;

    private HelloCacheService helloCacheService;

    private HelloEvent testEvent;
    private String testEventJson;

    @BeforeEach
    void setUp() {
        // 設定 cacheConfig 的預設行為
        when(cacheConfig.getPrimaryTtl()).thenReturn(Duration.ofMinutes(30));
        when(cacheConfig.getTaskIndexTtl()).thenReturn(Duration.ofHours(24));
        when(cacheConfig.getLockTimeout()).thenReturn(Duration.ofSeconds(10));
        
        helloCacheService = new HelloCacheService(cacheRepository, jsonUtils, cacheConfig);
        
        // 準備測試資料
        Hello hello = new Hello();
        hello.setMessage("Test Message");
        testEvent = new HelloEvent(hello);
        testEventJson = "{\"id\":\"" + testEvent.getId() + "\",\"data\":{\"message\":\"Test Message\"}}";
    }

    @Test
    void cacheHelloEvent_ShouldReturnTrueWhenSuccessful() {
        // Arrange
        when(jsonUtils.modelToJsonStringSafe(testEvent)).thenReturn(Optional.of(testEventJson));

        // Act
        boolean result = helloCacheService.cacheHelloEvent(testEvent);

        // Assert
        assertTrue(result);
        verify(cacheRepository, times(2)).saveOrUpdateObject(anyString(), eq(testEventJson), 
            anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void cacheHelloEvent_ShouldReturnFalseWhenJsonSerializationFails() {
        // Arrange
        when(jsonUtils.modelToJsonStringSafe(testEvent)).thenReturn(Optional.empty());

        // Act
        boolean result = helloCacheService.cacheHelloEvent(testEvent);

        // Assert
        assertFalse(result);
    }

    @Test
    void cacheHelloEvent_ShouldReturnFalseWhenCacheOperationFails() {
        // Arrange
        when(jsonUtils.modelToJsonStringSafe(testEvent)).thenReturn(Optional.of(testEventJson));
        doThrow(new RuntimeException("Cache error")).when(cacheRepository)
            .saveOrUpdateObject(anyString(), anyString(), anyLong(), any(TimeUnit.class));

        // Act
        boolean result = helloCacheService.cacheHelloEvent(testEvent);

        // Assert
        assertFalse(result);
    }

    @Test
    void getHelloEvent_ShouldReturnEventWhenFoundInCache() {
        // Arrange
        String primaryKey = KindConstants.PLATFORM_HELLO + ":" + testEvent.getId();
        when(cacheRepository.retrieveObject(primaryKey)).thenReturn(Optional.of(testEventJson));
        when(jsonUtils.jsonStringToModelSafe(testEventJson, HelloEvent.class))
            .thenReturn(Optional.of(testEvent));

        // Act
        Optional<HelloEvent> result = helloCacheService.getHelloEvent(testEvent.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testEvent.getId(), result.get().getId());
    }

    @Test
    void getHelloEvent_ShouldReturnEmptyWhenNotFoundInCache() {
        // Arrange
        String primaryKey = KindConstants.PLATFORM_HELLO + ":" + testEvent.getId();
        when(cacheRepository.retrieveObject(primaryKey)).thenReturn(Optional.empty());

        // Act
        Optional<HelloEvent> result = helloCacheService.getHelloEvent(testEvent.getId());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getHelloEvent_ShouldReturnEmptyWhenDeserializationFails() {
        // Arrange
        String primaryKey = KindConstants.PLATFORM_HELLO + ":" + testEvent.getId();
        when(cacheRepository.retrieveObject(primaryKey)).thenReturn(Optional.of(testEventJson));
        when(jsonUtils.jsonStringToModelSafe(testEventJson, HelloEvent.class))
            .thenReturn(Optional.empty());

        // Act
        Optional<HelloEvent> result = helloCacheService.getHelloEvent(testEvent.getId());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getHelloEventFromTaskIndex_ShouldReturnEventWhenFoundInTaskIndex() {
        // Arrange
        String taskIndexKey = KindConstants.SYS_TASK + ":" + testEvent.getId();
        when(cacheRepository.retrieveObject(taskIndexKey)).thenReturn(Optional.of(testEventJson));
        when(jsonUtils.jsonStringToModelSafe(testEventJson, HelloEvent.class))
            .thenReturn(Optional.of(testEvent));

        // Act
        Optional<HelloEvent> result = helloCacheService.getHelloEventFromTaskIndex(testEvent.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testEvent.getId(), result.get().getId());
    }

    @Test
    void updateHelloEvent_ShouldReturnTrueWhenSuccessful() {
        // Arrange
        when(jsonUtils.modelToJsonStringSafe(testEvent)).thenReturn(Optional.of(testEventJson));

        // Act
        boolean result = helloCacheService.updateHelloEvent(testEvent);

        // Assert
        assertTrue(result);
        verify(cacheRepository, times(2)).saveOrUpdateObject(anyString(), eq(testEventJson), 
            anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void deleteHelloEvent_ShouldReturnTrueWhenSuccessful() {
        // Act
        boolean result = helloCacheService.deleteHelloEvent(testEvent.getId());

        // Assert
        assertTrue(result);
        verify(cacheRepository, times(2)).delete(anyString());
    }

    @Test
    void deleteHelloEvent_ShouldReturnFalseWhenCacheOperationFails() {
        // Arrange
        doThrow(new RuntimeException("Cache error")).when(cacheRepository).delete(anyString());

        // Act
        boolean result = helloCacheService.deleteHelloEvent(testEvent.getId());

        // Assert
        assertFalse(result);
    }

    @Test
    void existsInCache_ShouldReturnTrueWhenEventExists() {
        // Arrange
        String primaryKey = KindConstants.PLATFORM_HELLO + ":" + testEvent.getId();
        when(cacheRepository.retrieveObject(primaryKey)).thenReturn(Optional.of(testEventJson));

        // Act
        boolean result = helloCacheService.existsInCache(testEvent.getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void existsInCache_ShouldReturnFalseWhenEventDoesNotExist() {
        // Arrange
        String primaryKey = KindConstants.PLATFORM_HELLO + ":" + testEvent.getId();
        when(cacheRepository.retrieveObject(primaryKey)).thenReturn(Optional.empty());

        // Act
        boolean result = helloCacheService.existsInCache(testEvent.getId());

        // Assert
        assertFalse(result);
    }

    @Test
    void executeWithLock_ShouldExecuteOperationWhenLockAcquired() {
        // Arrange
        String lockKey = KindConstants.PLATFORM_HELLO + ":" + testEvent.getId() + ":lock";
        when(cacheRepository.setIfNotExists(eq(lockKey), anyString(), any()))
            .thenReturn(true);

        // Act
        Optional<String> result = helloCacheService.executeWithLock(testEvent.getId(), 
            () -> "operation result");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("operation result", result.get());
        verify(cacheRepository).delete(lockKey);
    }

    @Test
    void executeWithLock_ShouldReturnEmptyWhenLockNotAcquired() {
        // Arrange
        String lockKey = KindConstants.PLATFORM_HELLO + ":" + testEvent.getId() + ":lock";
        when(cacheRepository.setIfNotExists(eq(lockKey), anyString(), any()))
            .thenReturn(false);

        // Act
        Optional<String> result = helloCacheService.executeWithLock(testEvent.getId(), 
            () -> "operation result");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void clearAllHelloCache_ShouldDeleteAllCacheEntries() {
        // Arrange
        Set<String> primaryKeys = Set.of("platform:hello:1", "platform:hello:2");
        Set<String> taskIndexKeys = Set.of("sys:task:1", "sys:task:2");
        
        when(cacheRepository.findKeysByPattern(KindConstants.PLATFORM_HELLO + ":*"))
            .thenReturn(primaryKeys);
        when(cacheRepository.findKeysByPattern(KindConstants.SYS_TASK + ":*"))
            .thenReturn(taskIndexKeys);

        // Act
        helloCacheService.clearAllHelloCache();

        // Assert
        verify(cacheRepository, times(4)).delete(anyString());
    }
}
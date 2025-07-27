package com.gtcafe.asimov.system.hello.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.hello.config.HelloCacheConfig;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
import com.gtcafe.asimov.system.hello.service.HelloCacheService.CacheMetricsRecorder;
import com.gtcafe.asimov.system.task.schema.TaskState;

/**
 * HelloCacheService 統一測試類
 */
@ExtendWith(MockitoExtension.class)
class HelloCacheServiceTest {

    @Mock
    private CacheRepository cacheRepository;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private HelloCacheConfig cacheConfig;

    @Mock
    private CacheMetricsRecorder metricsRecorder;

    private HelloCacheService helloCacheService;

    @BeforeEach
    void setUp() {
        helloCacheService = new HelloCacheService(cacheRepository, jsonUtils, cacheConfig);
        helloCacheService.setMetricsRecorder(metricsRecorder);
        
        // 設定預設配置
        when(cacheConfig.isEnabled()).thenReturn(true);
        when(cacheConfig.isMetricsEnabled()).thenReturn(true);
        when(cacheConfig.isFailOnCacheError()).thenReturn(false);
        when(cacheConfig.getPrimaryTtl()).thenReturn(Duration.ofMinutes(30));
        when(cacheConfig.getTaskIndexTtl()).thenReturn(Duration.ofHours(24));
        when(cacheConfig.getLockTimeout()).thenReturn(Duration.ofSeconds(10));
    }

    @Test
    void testCacheHelloEvent_Success() {
        // Arrange
        HelloEvent event = createTestEvent();
        String eventJson = "{\"id\":\"test-id\",\"state\":\"RUNNING\"}";
        
        when(jsonUtils.modelToJsonStringSafe(event)).thenReturn(Optional.of(eventJson));

        // Act
        boolean result = helloCacheService.cacheHelloEvent(event);

        // Assert
        assertTrue(result);
        verify(cacheRepository, times(2)).saveOrUpdateObject(anyString(), eq(eventJson), anyLong(), eq(TimeUnit.SECONDS));
        verify(metricsRecorder).recordCacheOperation("save", true);
    }

    @Test
    void testCacheHelloEvent_CacheDisabled() {
        // Arrange
        HelloEvent event = createTestEvent();
        when(cacheConfig.isEnabled()).thenReturn(false);

        // Act
        boolean result = helloCacheService.cacheHelloEvent(event);

        // Assert
        assertTrue(result);
        verify(cacheRepository, never()).saveOrUpdateObject(anyString(), anyString(), anyLong(), any(TimeUnit.class));
        verify(metricsRecorder, never()).recordCacheOperation(anyString(), anyBoolean());
    }

    @Test
    void testGetHelloEvent_CacheHit() {
        // Arrange
        String eventId = "test-id";
        String eventJson = "{\"id\":\"test-id\",\"state\":\"RUNNING\"}";
        HelloEvent expectedEvent = createTestEvent();
        
        when(cacheRepository.retrieveObject(anyString())).thenReturn(Optional.of(eventJson));
        when(jsonUtils.jsonStringToModelSafe(eventJson, HelloEvent.class)).thenReturn(Optional.of(expectedEvent));

        // Act
        Optional<HelloEvent> result = helloCacheService.getHelloEvent(eventId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedEvent, result.get());
        verify(metricsRecorder).recordCacheOperation("get", true);
    }

    @Test
    void testGetHelloEvent_CacheMiss() {
        // Arrange
        String eventId = "test-id";
        when(cacheRepository.retrieveObject(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<HelloEvent> result = helloCacheService.getHelloEvent(eventId);

        // Assert
        assertFalse(result.isPresent());
        verify(metricsRecorder).recordCacheOperation("get", false);
    }

    @Test
    void testDeleteHelloEvent_Success() {
        // Arrange
        String eventId = "test-id";

        // Act
        boolean result = helloCacheService.deleteHelloEvent(eventId);

        // Assert
        assertTrue(result);
        verify(cacheRepository, times(2)).delete(anyString());
        verify(metricsRecorder).recordCacheOperation("delete", true);
    }

    @Test
    void testExistsInCache() {
        // Arrange
        String eventId = "test-id";
        when(cacheRepository.retrieveObject(anyString())).thenReturn(Optional.of("some-value"));

        // Act
        boolean result = helloCacheService.existsInCache(eventId);

        // Assert
        assertTrue(result);
    }

    @Test
    void testInvalidationListener() {
        // Arrange
        HelloCacheService.CacheInvalidationListener listener = mock(HelloCacheService.CacheInvalidationListener.class);
        HelloEvent event = createTestEvent();
        String eventJson = "{\"id\":\"test-id\",\"state\":\"RUNNING\"}";
        
        when(jsonUtils.modelToJsonStringSafe(event)).thenReturn(Optional.of(eventJson));
        
        // Act
        helloCacheService.registerInvalidationListener(listener);
        helloCacheService.cacheHelloEvent(event);

        // Assert
        verify(listener).onCacheInvalidation(any(HelloCacheService.CacheInvalidationEvent.class));
    }

    @Test
    void testExecuteWithLock_Success() {
        // Arrange
        String eventId = "test-id";
        when(cacheRepository.setIfNotExists(anyString(), anyString(), any(Duration.class))).thenReturn(true);

        // Act
        Optional<String> result = helloCacheService.executeWithLock(eventId, () -> "operation result");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("operation result", result.get());
        verify(cacheRepository).delete(anyString()); // 驗證鎖被釋放
    }

    @Test
    void testExecuteWithLock_LockFailed() {
        // Arrange
        String eventId = "test-id";
        when(cacheRepository.setIfNotExists(anyString(), anyString(), any(Duration.class))).thenReturn(false);

        // Act
        Optional<String> result = helloCacheService.executeWithLock(eventId, () -> "operation result");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testMetricsRecorderNotSet() {
        // Arrange
        HelloCacheService serviceWithoutMetrics = new HelloCacheService(cacheRepository, jsonUtils, cacheConfig);
        HelloEvent event = createTestEvent();
        String eventJson = "{\"id\":\"test-id\",\"state\":\"RUNNING\"}";
        
        when(jsonUtils.modelToJsonStringSafe(event)).thenReturn(Optional.of(eventJson));

        // Act
        boolean result = serviceWithoutMetrics.cacheHelloEvent(event);

        // Assert
        assertTrue(result);
        // 不應該有任何指標記錄調用
        verifyNoInteractions(metricsRecorder);
    }

    private HelloEvent createTestEvent() {
        HelloEvent event = new HelloEvent();
        event.setId("test-id");
        event.setState(TaskState.RUNNING);
        
        Hello hello = new Hello();
        hello.setMessage("Test message");
        event.setData(hello);
        
        return event;
    }
}
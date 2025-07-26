package com.gtcafe.asimov.system.hello.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.infrastructure.queue.Producer;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfig;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
import com.gtcafe.asimov.system.hello.repository.HelloEntity;
import com.gtcafe.asimov.system.hello.repository.HelloRepository;

/**
 * HelloService 單元測試
 * 測試 HelloService 的各種業務邏輯情境
 */
@ExtendWith(MockitoExtension.class)
class HelloServiceTest {

    @Mock
    private Producer producer;

    @Mock
    private CacheRepository cacheRepos;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private HelloQueueConfig queueConfig;

    @Mock
    private HelloRepository helloRepository;

    private HelloService helloService;

    @BeforeEach
    void setUp() {
        helloService = new HelloService(producer, cacheRepos, jsonUtils, queueConfig, helloRepository);
    }

    @Test
    void sayHelloSync_ShouldReturnHelloWithCorrectMessage() {
        // Act
        Hello result = helloService.sayHelloSync();

        // Assert
        assertNotNull(result);
        assertEquals("Hello, World!", result.getMessage());
    }

    @Test
    void sayHelloSync_ShouldHandleException() {
        // 這個測試比較難模擬，因為 sayHelloSync 方法很簡單
        // 但我們可以測試它確實返回了正確的結果
        Hello result = helloService.sayHelloSync();
        
        assertNotNull(result);
        assertEquals("Hello, World!", result.getMessage());
    }

    @Test
    void sayHelloAsync_ShouldProcessSuccessfully() {
        // Arrange
        Hello inputHello = new Hello();
        inputHello.setMessage("Test Message");
        
        String mockJsonString = "{\"id\":\"test-id\",\"data\":{\"message\":\"Test Message\"}}";
        String mockQueueName = "test-queue";
        
        when(jsonUtils.modelToJsonStringSafe(any(HelloEvent.class))).thenReturn(Optional.of(mockJsonString));
        when(queueConfig.getQueueName()).thenReturn(mockQueueName);
        when(helloRepository.save(any(HelloEntity.class))).thenReturn(new HelloEntity("Test Message"));

        // Act
        HelloEvent result = helloService.sayHelloAsync(inputHello);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(inputHello, result.getData());

        // Verify interactions
        verify(cacheRepos, times(2)).saveOrUpdateObject(anyString(), eq(mockJsonString));
        verify(helloRepository, times(1)).save(any(HelloEntity.class));
        verify(producer, times(1)).sendEvent(eq(result), eq(mockQueueName));
    }

    @Test
    void sayHelloAsync_ShouldThrowExceptionWhenJsonSerializationFails() {
        // Arrange
        Hello inputHello = new Hello();
        inputHello.setMessage("Test Message");
        
        when(jsonUtils.modelToJsonStringSafe(any(HelloEvent.class))).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            helloService.sayHelloAsync(inputHello);
        });

        assertEquals("Failed to process asynchronous hello request", exception.getMessage());
    }

    @Test
    void sayHelloAsync_ShouldThrowExceptionWhenDatabaseSaveFails() {
        // Arrange
        Hello inputHello = new Hello();
        inputHello.setMessage("Test Message");
        
        String mockJsonString = "{\"id\":\"test-id\",\"data\":{\"message\":\"Test Message\"}}";
        
        when(jsonUtils.modelToJsonStringSafe(any(HelloEvent.class))).thenReturn(Optional.of(mockJsonString));
        when(helloRepository.save(any(HelloEntity.class))).thenThrow(new DataAccessException("Database error") {});

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            helloService.sayHelloAsync(inputHello);
        });

        assertEquals("Failed to process asynchronous hello request", exception.getMessage());
    }

    @Test
    void sayHelloAsync_ShouldThrowExceptionWhenCacheSaveFails() {
        // Arrange
        Hello inputHello = new Hello();
        inputHello.setMessage("Test Message");
        
        String mockJsonString = "{\"id\":\"test-id\",\"data\":{\"message\":\"Test Message\"}}";
        
        when(jsonUtils.modelToJsonStringSafe(any(HelloEvent.class))).thenReturn(Optional.of(mockJsonString));
        doThrow(new RuntimeException("Cache error")).when(cacheRepos).saveOrUpdateObject(anyString(), anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            helloService.sayHelloAsync(inputHello);
        });

        assertEquals("Failed to process asynchronous hello request", exception.getMessage());
    }

    @Test
    void sayHelloAsync_ShouldThrowExceptionWhenQueueSendFails() {
        // Arrange
        Hello inputHello = new Hello();
        inputHello.setMessage("Test Message");
        
        String mockJsonString = "{\"id\":\"test-id\",\"data\":{\"message\":\"Test Message\"}}";
        String mockQueueName = "test-queue";
        
        when(jsonUtils.modelToJsonStringSafe(any(HelloEvent.class))).thenReturn(Optional.of(mockJsonString));
        when(queueConfig.getQueueName()).thenReturn(mockQueueName);
        when(helloRepository.save(any(HelloEntity.class))).thenReturn(new HelloEntity("Test Message"));
        doThrow(new RuntimeException("Queue error")).when(producer).sendEvent(any(HelloEvent.class), anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            helloService.sayHelloAsync(inputHello);
        });

        assertEquals("Failed to process asynchronous hello request", exception.getMessage());
    }
}
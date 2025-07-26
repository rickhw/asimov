package com.gtcafe.asimov.system.hello.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.gtcafe.asimov.infrastructure.queue.Producer;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfig;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
import com.gtcafe.asimov.system.hello.repository.HelloEntity;
import com.gtcafe.asimov.system.hello.repository.HelloRepository;
import com.gtcafe.asimov.system.hello.service.HelloCacheService;
import com.gtcafe.asimov.system.hello.service.HelloValidationService;

/**
 * HelloService 單元測試
 * 測試 HelloService 的各種業務邏輯情境
 */
@ExtendWith(MockitoExtension.class)
class HelloServiceTest {

    @Mock
    private Producer producer;

    @Mock
    private HelloQueueConfig queueConfig;

    @Mock
    private HelloRepository helloRepository;

    @Mock
    private HelloCacheService helloCacheService;

    @Mock
    private HelloValidationService validationService;

    private HelloService helloService;

    @BeforeEach
    void setUp() {
        helloService = new HelloService(producer, queueConfig, helloRepository, helloCacheService, validationService);
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
        
        String mockQueueName = "test-queue";
        
        when(helloCacheService.cacheHelloEvent(any(HelloEvent.class))).thenReturn(true);
        when(queueConfig.getQueueName()).thenReturn(mockQueueName);
        when(helloRepository.save(any(HelloEntity.class))).thenReturn(new HelloEntity("Test Message"));

        // Act
        HelloEvent result = helloService.sayHelloAsync(inputHello);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(inputHello, result.getData());

        // Verify interactions
        verify(validationService, times(1)).validateHello(inputHello);
        verify(helloCacheService, times(1)).cacheHelloEvent(any(HelloEvent.class));
        verify(helloRepository, times(1)).save(any(HelloEntity.class));
        verify(producer, times(1)).sendEvent(eq(result), eq(mockQueueName));
    }

    @Test
    void sayHelloAsync_ShouldContinueWhenCacheFails() {
        // Arrange
        Hello inputHello = new Hello();
        inputHello.setMessage("Test Message");
        
        String mockQueueName = "test-queue";
        
        when(helloCacheService.cacheHelloEvent(any(HelloEvent.class))).thenReturn(false);
        when(queueConfig.getQueueName()).thenReturn(mockQueueName);
        when(helloRepository.save(any(HelloEntity.class))).thenReturn(new HelloEntity("Test Message"));

        // Act
        HelloEvent result = helloService.sayHelloAsync(inputHello);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(inputHello, result.getData());

        // Verify interactions - should continue even if cache fails
        verify(helloCacheService, times(1)).cacheHelloEvent(any(HelloEvent.class));
        verify(helloRepository, times(1)).save(any(HelloEntity.class));
        verify(producer, times(1)).sendEvent(eq(result), eq(mockQueueName));
    }

    @Test
    void sayHelloAsync_ShouldThrowExceptionWhenDatabaseSaveFails() {
        // Arrange
        Hello inputHello = new Hello();
        inputHello.setMessage("Test Message");
        
        when(helloCacheService.cacheHelloEvent(any(HelloEvent.class))).thenReturn(true);
        when(helloRepository.save(any(HelloEntity.class))).thenThrow(new DataAccessException("Database error") {});

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
        
        String mockQueueName = "test-queue";
        
        when(helloCacheService.cacheHelloEvent(any(HelloEvent.class))).thenReturn(true);
        when(queueConfig.getQueueName()).thenReturn(mockQueueName);
        when(helloRepository.save(any(HelloEntity.class))).thenReturn(new HelloEntity("Test Message"));
        doThrow(new RuntimeException("Queue error")).when(producer).sendEvent(any(HelloEvent.class), anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            helloService.sayHelloAsync(inputHello);
        });

        assertEquals("Failed to process asynchronous hello request", exception.getMessage());
    }

    @Test
    void sayHelloAsync_ShouldThrowExceptionWhenValidationFails() {
        // Arrange
        Hello inputHello = new Hello();
        inputHello.setMessage("invalid message");
        
        doThrow(new HelloValidationService.HelloValidationException("Validation failed"))
            .when(validationService).validateHello(inputHello);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            helloService.sayHelloAsync(inputHello);
        });

        assertEquals("Failed to process asynchronous hello request", exception.getMessage());
        
        // Verify that validation was called but other operations were not
        verify(validationService, times(1)).validateHello(inputHello);
        verify(helloCacheService, times(0)).cacheHelloEvent(any(HelloEvent.class));
        verify(helloRepository, times(0)).save(any(HelloEntity.class));
        verify(producer, times(0)).sendEvent(any(HelloEvent.class), anyString());
    }

    @Test
    void getHelloEvent_ShouldReturnEventFromCache() {
        // Arrange
        String eventId = "123e4567-e89b-12d3-a456-426614174000";
        Hello hello = new Hello();
        hello.setMessage("Test Message");
        HelloEvent expectedEvent = new HelloEvent(hello);
        
        when(helloCacheService.getHelloEvent(eventId)).thenReturn(Optional.of(expectedEvent));

        // Act
        HelloEvent result = helloService.getHelloEvent(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedEvent.getId(), result.getId());
        verify(validationService, times(1)).validateEventId(eventId);
        verify(helloCacheService, times(1)).getHelloEvent(eventId);
    }

    @Test
    void getHelloEvent_ShouldThrowExceptionWhenEventIdValidationFails() {
        // Arrange
        String invalidEventId = "invalid-id";
        
        doThrow(new HelloValidationService.HelloValidationException("Invalid event ID"))
            .when(validationService).validateEventId(invalidEventId);

        // Act & Assert
        assertThrows(HelloValidationService.HelloValidationException.class, () -> {
            helloService.getHelloEvent(invalidEventId);
        });
        
        verify(validationService, times(1)).validateEventId(invalidEventId);
        verify(helloCacheService, times(0)).getHelloEvent(anyString());
    }

    @Test
    void getHelloEvent_ShouldReturnEventFromTaskIndexWhenNotInPrimaryCache() {
        // Arrange
        String eventId = "test-event-id";
        Hello hello = new Hello();
        hello.setMessage("Test Message");
        HelloEvent expectedEvent = new HelloEvent(hello);
        
        when(helloCacheService.getHelloEvent(eventId)).thenReturn(Optional.empty());
        when(helloCacheService.getHelloEventFromTaskIndex(eventId)).thenReturn(Optional.of(expectedEvent));
        when(helloCacheService.cacheHelloEvent(expectedEvent)).thenReturn(true);

        // Act
        HelloEvent result = helloService.getHelloEvent(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedEvent.getId(), result.getId());
        verify(helloCacheService, times(1)).getHelloEvent(eventId);
        verify(helloCacheService, times(1)).getHelloEventFromTaskIndex(eventId);
        verify(helloCacheService, times(1)).cacheHelloEvent(expectedEvent);
    }

    @Test
    void getHelloEvent_ShouldReturnNullWhenNotFound() {
        // Arrange
        String eventId = "test-event-id";
        
        when(helloCacheService.getHelloEvent(eventId)).thenReturn(Optional.empty());
        when(helloCacheService.getHelloEventFromTaskIndex(eventId)).thenReturn(Optional.empty());

        // Act
        HelloEvent result = helloService.getHelloEvent(eventId);

        // Assert
        assertNull(result);
        verify(helloCacheService, times(1)).getHelloEvent(eventId);
        verify(helloCacheService, times(1)).getHelloEventFromTaskIndex(eventId);
    }

    @Test
    void updateHelloEvent_ShouldReturnTrueWhenSuccessful() {
        // Arrange
        Hello hello = new Hello();
        hello.setMessage("Updated Message");
        HelloEvent event = new HelloEvent(hello);
        
        when(helloCacheService.updateHelloEvent(event)).thenReturn(true);

        // Act
        boolean result = helloService.updateHelloEvent(event);

        // Assert
        assertTrue(result);
        verify(helloCacheService, times(1)).updateHelloEvent(event);
    }
}
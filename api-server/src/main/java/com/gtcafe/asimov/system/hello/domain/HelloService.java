package com.gtcafe.asimov.system.hello.domain;

import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtcafe.asimov.infrastructure.queue.Producer;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfig;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
import com.gtcafe.asimov.system.hello.repository.HelloEntity;
import com.gtcafe.asimov.system.hello.repository.HelloRepository;
import com.gtcafe.asimov.system.hello.service.HelloCacheService;
import com.gtcafe.asimov.system.hello.service.HelloValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello 業務邏輯服務
 * 提供同步和非同步的 Hello 處理功能
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HelloService {

  private final Producer producer;
  private final HelloQueueConfig queueConfig;
  private final HelloRepository helloRepository;
  private final HelloCacheService helloCacheService;
  private final HelloValidationService validationService;

  /**
   * 同步處理 Hello 請求
   * 
   * @return Hello 物件
   */
  public Hello sayHelloSync() {
    log.info("Processing synchronous hello request");

    try {
      Hello hello = new Hello();
      hello.setMessage("Hello, World!");

      log.debug("Created hello message: {}", hello.getMessage());
      return hello;
    } catch (Exception e) {
      log.error("Error processing synchronous hello request", e);
      throw new RuntimeException("Failed to process hello request", e);
    }
  }

  /**
   * 非同步處理 Hello 請求
   * 
   * @param hello Hello 物件
   * @return HelloEvent 事件物件
   * @throws HelloValidationService.HelloValidationException 當輸入驗證失敗時拋出
   */
  @Transactional
  @Retryable(value = DataAccessException.class, maxAttempts = 3)
  public HelloEvent sayHelloAsync(Hello hello) {
    log.info("Processing asynchronous hello request with message: {}", hello.getMessage());

    try {
      // 0. validate input
      validationService.validateHello(hello);
      log.debug("Input validation passed for hello message");

      // 1. create a event
      HelloEvent event = new HelloEvent(hello);
      log.debug("Created hello event with ID: {}", event.getId());

      // 2. store to cache using HelloCacheService
      boolean cacheSuccess = helloCacheService.cacheHelloEvent(event);
      if (!cacheSuccess) {
        log.warn("Failed to cache hello event, but continuing with processing");
      }

      // 3. store to database
      HelloEntity entity = new HelloEntity(hello.getMessage());
      helloRepository.save(entity);
      log.debug("Stored hello entity to database with ID: {}", entity.getId());

      // 4. send to queue
      producer.sendEvent(event, queueConfig.getQueueName());
      log.debug("Sent hello event to queue: {}", queueConfig.getQueueName());

      log.info("Successfully processed asynchronous hello request with event ID: {}",
          event.getId());
      return event;

    } catch (Exception e) {
      log.error("Error processing asynchronous hello request for message: {}", hello.getMessage(),
          e);
      throw new RuntimeException("Failed to process asynchronous hello request", e);
    }
  }

  /**
   * 檢索 HelloEvent
   * 先從快取檢索，如果不存在則從資料庫載入
   * 
   * @param eventId 事件 ID
   * @return HelloEvent 物件，如果不存在則返回 null
   * @throws HelloValidationService.HelloValidationException 當事件 ID 驗證失敗時拋出
   */
  public HelloEvent getHelloEvent(String eventId) {
    log.debug("Retrieving hello event with ID: {}", eventId);

    try {
      // validate event ID
      validationService.validateEventId(eventId);
      log.debug("Event ID validation passed");
      // 1. 先從快取檢索
      var cachedEvent = helloCacheService.getHelloEvent(eventId);
      if (cachedEvent.isPresent()) {
        log.debug("Found hello event in cache with ID: {}", eventId);
        return cachedEvent.get();
      }

      // 2. 如果快取中沒有，嘗試從任務索引快取檢索
      var taskIndexEvent = helloCacheService.getHelloEventFromTaskIndex(eventId);
      if (taskIndexEvent.isPresent()) {
        log.debug("Found hello event in task index cache with ID: {}", eventId);
        // 重新快取到主要快取
        helloCacheService.cacheHelloEvent(taskIndexEvent.get());
        return taskIndexEvent.get();
      }

      log.debug("Hello event not found in cache with ID: {}", eventId);
      return null;

    } catch (Exception e) {
      log.error("Error retrieving hello event with ID: {}", eventId, e);
      return null;
    }
  }

  /**
   * 更新 HelloEvent 狀態
   * 
   * @param event 更新後的 HelloEvent
   * @return 是否更新成功
   * @throws HelloValidationService.HelloValidationException 當事件驗證失敗時拋出
   */
  public boolean updateHelloEvent(HelloEvent event) {
    log.debug("Updating hello event with ID: {}", event.getId());

    try {
      // validate event
      if (event == null) {
        throw new HelloValidationService.HelloValidationException("HelloEvent cannot be null");
      }
      
      validationService.validateEventId(event.getId());
      
      if (event.getData() != null) {
        validationService.validateHello(event.getData());
      }
      
      log.debug("Event validation passed");
      // 更新快取
      boolean cacheSuccess = helloCacheService.updateHelloEvent(event);
      if (!cacheSuccess) {
        log.warn("Failed to update hello event in cache with ID: {}", event.getId());
      }

      log.debug("Successfully updated hello event with ID: {}", event.getId());
      return true;

    } catch (Exception e) {
      log.error("Error updating hello event with ID: {}", event.getId(), e);
      return false;
    }
  }
}

package com.gtcafe.asimov.system.hello.domain;

import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.infrastructure.queue.Producer;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfig;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
import com.gtcafe.asimov.system.hello.repository.HelloEntity;
import com.gtcafe.asimov.system.hello.repository.HelloRepository;

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
  private final CacheRepository cacheRepos;
  private final JsonUtils jsonUtils;
  private final HelloQueueConfig _qconfig;
  private final HelloRepository helloRepository;

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
   */
  @Transactional
  @Retryable(value = DataAccessException.class, maxAttempts = 3)
  public HelloEvent sayHelloAsync(Hello hello) {
    log.info("Processing asynchronous hello request with message: {}", hello.getMessage());

    try {
      // 1. create a event
      HelloEvent event = new HelloEvent(hello);
      String cachedKey = String.format("%s:%s", KindConstants.PLATFORM_HELLO, event.getId());
      String taskCachedKeyForIndex = String.format("%s:%s", KindConstants.SYS_TASK, event.getId());

      log.debug("Created hello event with ID: {}", event.getId());

      // 2. store to cache
      String taskJsonString = jsonUtils.modelToJsonStringSafe(event)
          .orElseThrow(() -> new RuntimeException("Failed to serialize hello event to JSON"));

      cacheRepos.saveOrUpdateObject(cachedKey, taskJsonString);
      cacheRepos.saveOrUpdateObject(taskCachedKeyForIndex, taskJsonString);
      log.debug("Stored hello event to cache with keys: {} and {}", cachedKey,
          taskCachedKeyForIndex);

      // 3. store to database
      HelloEntity entity = new HelloEntity(hello.getMessage());
      helloRepository.save(entity);
      log.debug("Stored hello entity to database with ID: {}", entity.getId());

      // 4. send to queue
      producer.sendEvent(event, _qconfig.getQueueName());
      log.debug("Sent hello event to queue: {}", _qconfig.getQueueName());

      log.info("Successfully processed asynchronous hello request with event ID: {}",
          event.getId());
      return event;

    } catch (Exception e) {
      log.error("Error processing asynchronous hello request for message: {}", hello.getMessage(),
          e);
      throw new RuntimeException("Failed to process asynchronous hello request", e);
    }
  }
}

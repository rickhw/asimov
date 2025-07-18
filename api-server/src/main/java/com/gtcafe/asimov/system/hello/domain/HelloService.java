package com.gtcafe.asimov.system.hello.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.constants.QueueName;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.framework.utils.TimeUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.infrastructure.queue.Producer;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfig;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloService {

  @Autowired
  private Producer producer;

  @Autowired
  private CacheRepository cacheRepos;

  @Autowired
  private JsonUtils jsonUtils;

  @Autowired
  private HelloQueueConfig _qconfig;

  public Hello sayHelloSync() {

    Hello hello =  new Hello();
    hello.setMessage("Hello, World!");

    return hello;
  }

  public HelloEvent sayHelloAsync(Hello hello) {

    // 1. create a event
    HelloEvent event = new HelloEvent(hello);
    String cachedKey = String.format("%s:%s", KindConstants.PLATFORM_HELLO, event.getId());
    String taskCachedKeyForIndex = String.format("%s:%s", KindConstants.SYS_TASK, event.getId());

    // 2. store to cache
    String taskJsonString = jsonUtils.modelToJsonStringSafe(event).get();
    cacheRepos.saveOrUpdateObject(cachedKey, taskJsonString);
    cacheRepos.saveOrUpdateObject(taskCachedKeyForIndex, taskJsonString);

    // 3. store to db

    // 3. sent to queue
    producer.sendEvent(event, _qconfig.getQueueName());

    return event;
  }
}
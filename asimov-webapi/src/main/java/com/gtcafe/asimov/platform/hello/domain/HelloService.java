package com.gtcafe.asimov.platform.hello.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.hello.consumer.HelloEvent;
import com.gtcafe.asimov.platform.hello.model.Hello;
import com.gtcafe.asimov.platform.hello.rest.response.HelloTaskResponse;
import com.gtcafe.asimov.system.cache.CacheRepository;
import com.gtcafe.asimov.system.constants.KindConstants;
import com.gtcafe.asimov.system.constants.QueueName;
import com.gtcafe.asimov.system.queue.MessageProducer;
import com.gtcafe.asimov.system.utils.JsonUtils;
import com.gtcafe.asimov.system.utils.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloService {

  @Autowired
  private MessageProducer producer;

  @Autowired
  private CacheRepository cacheRepos;

  @Autowired
  private JsonUtils jsonUtils;

  @Autowired
  private TimeUtils timeUtils;

  // private static final String CACHE_KEY_PREFIEX = "";

  public Hello sayHelloSync() {

    Hello hello =  new Hello();
    hello.setMessage("Hello, World!");

    return hello;
  }

  public HelloEvent sayHelloAsync(Hello hello) {

    // create a event
    HelloEvent event = new HelloEvent(hello);
    String cachedKey = String.format("%s:%s", KindConstants.PLATFORM_HELLO, event.getId());
    String taskCachedKeyForIndex = String.format("%s:%s", KindConstants.SYS_TASK, event.getId());

    // sent to queue
    producer.sendEvent(event, QueueName.HELLO_QUEUE);


    String taskJsonString = jsonUtils.modelToJsonString(event);
    cacheRepos.saveOrUpdateObject(cachedKey, taskJsonString);
    cacheRepos.saveOrUpdateObject(taskCachedKeyForIndex, taskJsonString);

    return event;
  }
}
package com.gtcafe.asimov.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.common.utils.JsonUtils;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.platform.config.MessageProducer;
import com.gtcafe.asimov.core.system.cache.CacheRepository;
import com.gtcafe.asimov.core.system.constants.QueueName;

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

  public String sayHelloSync(String message) {
    // SayHelloEvent event = new SayHelloEvent(message);
    // producer.sendEvent(event, QueueName.SAY_HELLO_QUEUE_NAME);

    // String taskJsonString = jsonUtils.modelToJsonString(event);
    // cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);

    return message;
}

  public SayHelloEvent sayHelloAsync(String message) {
      SayHelloEvent event = new SayHelloEvent(message);
      producer.sendEvent(event, QueueName.HELLO_QUEUE);

      String taskJsonString = jsonUtils.modelToJsonString(event);
      cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);

      return event;
  }
}
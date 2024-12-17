package com.gtcafe.asimov.platform.hello.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.conifg.MessageProducer;
import com.gtcafe.asimov.core.platform.hello.Hello;
import com.gtcafe.asimov.core.platform.hello.HelloEvent;
import com.gtcafe.asimov.core.system.cache.CacheRepository;
import com.gtcafe.asimov.core.system.constants.QueueName;
import com.gtcafe.asimov.core.system.utils.JsonUtils;
import com.gtcafe.asimov.core.system.utils.TimeUtils;
import com.gtcafe.asimov.platform.hello.rest.response.HelloTaskResponse;

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

  public Hello sayHelloSync() {

    Hello hello =  Hello.builder()
      .message("Hello, World!")
      // .timestamp(timeUtils.currentTimeIso8601())
      .build();

    return hello;
  }

  public HelloTaskResponse sayHelloAsync(Hello hello) {
    HelloEvent event = new HelloEvent(hello);

    producer.sendEvent(event, QueueName.HELLO_QUEUE);

    String taskJsonString = jsonUtils.modelToJsonString(event);
    cacheRepos.saveOrUpdateObject(event.getEventId(), taskJsonString);

    HelloTaskResponse res = new HelloTaskResponse(hello);
    res.setCreationTime(timeUtils.currentTimeIso8601());

    return res;
  }
}
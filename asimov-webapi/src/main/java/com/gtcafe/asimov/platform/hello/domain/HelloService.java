package com.gtcafe.asimov.platform.hello.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.conifg.MessageProducer;
import com.gtcafe.asimov.core.platform.hello.Hello;
import com.gtcafe.asimov.core.platform.hello.HelloEvent;
import com.gtcafe.asimov.core.system.cache.CacheRepository;
import com.gtcafe.asimov.core.system.constants.QueueName;
import com.gtcafe.asimov.core.system.utils.JsonUtils;
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

  public Hello sayHelloSync() {

    Hello hello = new Hello("Hello, World!");

    return hello;
  }

  public HelloTaskResponse sayHelloAsync(Hello hello) {
    HelloEvent event = new HelloEvent(hello);
    producer.sendEvent(event, QueueName.HELLO_QUEUE);

    String taskJsonString = jsonUtils.modelToJsonString(event);
    cacheRepos.saveOrUpdateObject(event.getEventId(), taskJsonString);

    HelloTaskResponse res = new HelloTaskResponse(hello);

    return res;
  }
}
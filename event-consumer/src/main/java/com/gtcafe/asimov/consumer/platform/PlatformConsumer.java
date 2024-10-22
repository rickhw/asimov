package com.gtcafe.asimov.consumer.platform;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.constants.QueueName;
import com.gtcafe.asimov.core.domain.event.IMessage;
import com.gtcafe.asimov.core.platform.hello.SayHelloEventV4;
import com.gtcafe.asimov.core.platform.hello.SayHelloMessage;
import com.gtcafe.asimov.core.system.task.TaskState;
import com.gtcafe.asimov.core.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class PlatformConsumer { // implements MessageListener {

  @Autowired
  private JsonUtils _jsonUtils;

  @Autowired
  CacheRepository _cacheRepos;

  @Autowired
  HelloEventHandler _handler;
  
  @RabbitListener(queues = QueueName.SAY_HELLO_QUEUE_NAME)
  public void receiveHelloEvent(String eventString) {
    // SayHelloEvent event = _jsonUtils.jsonStringToModel(eventString, SayHelloEvent.class);
    SayHelloEventV4 event = _jsonUtils.jsonStringToModel(eventString, SayHelloEventV4.class);
    
    System.out.println("eventString: " + eventString);
    System.out.println("eventObject: " + event);

    // 1. Change Task State from PENDING to RUNNING
    event.transit(TaskState.RUNNING);

    // 1.1 Update to cache
    String taskJsonString = _jsonUtils.modelToJsonString(event);
    _cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);
    
    // 2. handle biz logic
    IMessage message = event.getData();
      
    if (message instanceof SayHelloMessage)
      // _handler.sayHello(event);
      _handler.sayHelloV4(event);

  }


  @RabbitListener(queues = QueueName.SAY_HELLO_QUEUE_NAME)
  public void receiveHelloEventV4(String eventString) {
    // SayHelloEvent event = _jsonUtils.jsonStringToModel(eventString, SayHelloEvent.class);
    SayHelloEventV4 event = _jsonUtils.jsonStringToModel(eventString, SayHelloEventV4.class);
    
    System.out.println("eventString: " + eventString);
    System.out.println("eventObject: " + event);

    // 1. Change Task State from PENDING to RUNNING
    // TaskDomainObject tdo = event.getTask();
    event.transit(TaskState.RUNNING);
    // tdo.getMetadata().set_state(TaskState.RUNNING);

    // 1.1 Update to cache
    String taskJsonString = _jsonUtils.modelToJsonString(event);
    _cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);
    

    // 2. handle biz logic
    IMessage message = event.getData();
      
    if (message instanceof SayHelloMessage)
      // _handler.sayHello(event);
      _handler.sayHelloV4(event);

  }
}

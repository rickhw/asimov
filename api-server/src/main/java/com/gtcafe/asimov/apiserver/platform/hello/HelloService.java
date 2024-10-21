package com.gtcafe.asimov.apiserver.platform.hello;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.hello.operation.HelloResponse;
import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.apiserver.system.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.apiserver.system.utils.Slogan;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.constants.QueueName;
import com.gtcafe.asimov.core.platform.hello.HelloEventV3;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.platform.hello.SayHelloMessage;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.utils.JsonUtils;

import java.util.Date;

@Service
@Slf4j
public class HelloService {

  @Autowired
  MessageProducer _producer;

  @Autowired
  CacheRepository _cacheRepos;

  @Autowired
  private Slogan _slogon;

  @Autowired
  private JsonUtils jsonUtils;

  public HelloService() {}

  public String handlerSync(String message) {
    // 處理核心商業邏輯
    message += ", " + new Date().toString();
    
    return message;
  }

  public TaskDomainObject handlerAsync(String message) {

    // 1. assemble domain object
    SayHelloMessage sayHello = new SayHelloMessage(message);
    HelloEventV3 event = new HelloEventV3(sayHello);
    

    // 2. sent message to queue, put to dedicated queue
    _producer.sendEvent(event, QueueName.SAY_HELLO_QUEUE_NAME);

    // 3. store task to cache
    TaskDomainObject tdo = event.getTask();
    String taskJsonString = jsonUtils.modelToJsonString(tdo);
    _cacheRepos.saveOrUpdateObject(tdo.getTaskId(), taskJsonString);
    
    // RetrieveTaskResponse res = new RetrieveTaskResponse(taskObj);
    return tdo;
  }

}

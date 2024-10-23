package com.gtcafe.asimov.apiserver.platform.hello;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.constants.QueueName;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.utils.JsonUtils;
import com.gtcafe.asimov.core.utils.Slogan;

import lombok.extern.slf4j.Slf4j;

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

  public String sayHelloSync(String message) {
    // 處理核心商業邏輯
    message += ", " + new Date().toString();
    
    return message;
  }

  public SayHelloEvent sayHelloAsync(String message) {

    // 1. assemble domain object
    // SayHelloEvent event = new SayHelloEvent(message);
    SayHelloEvent event = new SayHelloEvent(message);
    
    // 2. sent message to queue, put to dedicated queue
    _producer.sendSayHelloEvent(event, QueueName.SAY_HELLO_QUEUE_NAME);

    // 3. store task to cache
    // TaskDomainObject tdo = event.getTask();
    String taskJsonString = jsonUtils.modelToJsonString(event);
    _cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);
    
    // RetrieveTaskResponse res = new RetrieveTaskResponse(taskObj);
    return event;
  }

}

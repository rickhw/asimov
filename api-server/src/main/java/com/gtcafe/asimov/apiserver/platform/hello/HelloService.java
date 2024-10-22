package com.gtcafe.asimov.apiserver.platform.hello;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.apiserver.system.utils.Slogan;
import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.constants.QueueName;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.platform.hello.SayHelloEventV4;
import com.gtcafe.asimov.core.platform.hello.SayHelloMessage;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.utils.JsonUtils;

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

  public String handlerSync(String message) {
    // 處理核心商業邏輯
    message += ", " + new Date().toString();
    
    return message;
  }

  public SayHelloEventV4 handlerAsync(String message) {

    // 1. assemble domain object
    // SayHelloEvent event = new SayHelloEvent(message);
    SayHelloEventV4 event = new SayHelloEventV4(message);
    
    // 2. sent message to queue, put to dedicated queue
    _producer.sendEventV4(event, QueueName.SAY_HELLO_QUEUE_NAME);

    // 3. store task to cache
    // TaskDomainObject tdo = event.getTask();
    String taskJsonString = jsonUtils.modelToJsonString(event);
    _cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);
    
    // RetrieveTaskResponse res = new RetrieveTaskResponse(taskObj);
    return event;
  }

}

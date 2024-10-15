package com.gtcafe.asimov.apiserver.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.hello.operation.HelloResponse;
import com.gtcafe.asimov.apiserver.system.CacheRepository;
import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.apiserver.system.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.apiserver.system.utils.Slogan;
import com.gtcafe.asimov.core.platform.SayHelloMessage;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.utils.JsonUtils;

import java.util.Date;

@Service
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

  // TODO: 把 HTTP Messsage (Request/Response) 的東西搬出去
  public HelloResponse handlerSync(String message) {
    // 處理核心商業邏輯
    message += ", " + new Date().toString();
    HelloResponse res = new HelloResponse(message);

    return res;
  }

  public RetrieveTaskResponse handlerAsync(String message) {

    // 1-1. assemble task object
    TaskDomainObject taskObj = new TaskDomainObject();
    taskObj.setSpec(message);

    // 1-2. assemble domain object
    SayHelloMessage sayHello = new SayHelloMessage(message);

    // 2-1. sent message to queue
    _producer.sayHelloEvent(sayHello);
    _producer.sendTaskEvent(taskObj);

    // 3-1. store task to cache
    String taskJsonString = jsonUtils.modelToJsonString(taskObj);
    _cacheRepos.saveOrUpdateObject(taskObj.getTaskId(), taskJsonString);
    

    RetrieveTaskResponse res = new RetrieveTaskResponse(taskObj);
    return res;
  }

}

package com.gtcafe.asimov.apiserver.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.hello.operation.HelloRequest;
import com.gtcafe.asimov.apiserver.platform.hello.operation.HelloResponse;
import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.apiserver.system.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.apiserver.system.utils.Slogan;

import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.platform.QueueNameConstants;
import com.gtcafe.asimov.core.platform.SayHelloMessage;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;


@Service
public class HelloService {

  // @Autowired
  // private HelloProducer _producer;

  @Autowired
  MessageProducer _producer;


  @Autowired
  private Slogan utils;

  // 專注處理 Biz Logic,
  // 1. 不處理組 DTO 的任務
  // 2. 應該沒有其他的 import, 只有 POJO

  // TODO: 把 HTTP Messsage (Request/Response) 的東西搬出去
  public HelloResponse handler(HelloRequest request) {
    HelloResponse res = new HelloResponse(request.getMessage());

    return res;
  }


  // TODO: 把 HTTP Messsage (Request/Response) 的東西搬出去
  public HelloResponse handler(String message) {
    HelloResponse res = new HelloResponse(message);

    // 處理核心商業邏輯

    return res;
  }

  // ----
  public RetrieveTaskResponse handlerAsync(String message) {
    // HelloResponse res = new HelloResponse(message);

    // 1-1. constract task object
    TaskDomainObject taskObj = new TaskDomainObject();
    taskObj.setSpec(message);

    // 1-2. constract domain object 
    SayHelloMessage sayHello = new SayHelloMessage(message);

    // 2-1. sent message to queue
    _producer.sendEvent(QueueNameConstants.SAY_HELLO_QUEUE_NAME, EventType.REGISTER_TENANT, sayHello);

    _producer.sendTaskEvent(taskObj);

    // 3-1. store data to cache


    RetrieveTaskResponse res = new RetrieveTaskResponse(taskObj);


    return res;
  }


}

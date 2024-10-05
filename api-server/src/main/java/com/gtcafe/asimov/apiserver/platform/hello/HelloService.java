package com.gtcafe.asimov.apiserver.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.task.TaskDomainObject;
import com.gtcafe.asimov.apiserver.platform.task.TaskProducer;
import com.gtcafe.asimov.apiserver.utils.Slogan;
import com.gtcafe.asimov.core.platform.SayHelloMessage;

@Service
public class HelloService {

  @Autowired
  private HelloProducer _producer;

  @Autowired
  private TaskProducer _taskProducer;


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
  public TaskDomainObject handlerAsync(String message) {
    // HelloResponse res = new HelloResponse(message);
    TaskDomainObject taskObj = new TaskDomainObject();

    SayHelloMessage sayHello = new SayHelloMessage(message);

    _producer.sentSayHelloEvent(sayHello);
    _taskProducer.sendEvent(sayHello);

    return taskObj;
  }


}

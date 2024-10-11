package com.gtcafe.asimov.apiserver.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gtcafe.asimov.apiserver.platform.hello.operation.HelloResponse;
import com.gtcafe.asimov.apiserver.system.CacheRepositoryV1;
import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.apiserver.system.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.apiserver.system.utils.Slogan;
import com.gtcafe.asimov.core.platform.SayHelloMessage;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;

// 專注處理 Biz Logic,
// 1. 不處理組 DTO 的任務
// 2. 應該沒有其他的 import, 只有 POJO

@Service
public class HelloService {

  // @Autowired
  // private HelloProducer _producer;

  @Autowired
  MessageProducer _producer;

  // @Autowired
  // RedisTemplate<String, Object> _redisTemplate;

  @Autowired
  CacheRepositoryV1 _cacheRepos;

  @Autowired
  private Slogan utils;

  private final ObjectMapper objectMapper;

  public HelloService() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
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

    // 1-1. assemble task object
    TaskDomainObject taskObj = new TaskDomainObject();
    taskObj.setSpec(message);

    // 1-2. assemble domain object
    SayHelloMessage sayHello = new SayHelloMessage(message);

    // 2-1. sent message to queue
    _producer.sayHelloEvent(sayHello);
    _producer.sendTaskEvent(taskObj);

    // 3-1. store task to cache
    try {
      String taskJson = objectMapper.writeValueAsString(taskObj);
      // _redisTemplate.opsForValue().set(taskObj.getTaskId(), taskJson);

      _cacheRepos.saveOrUpdateObject(taskObj.getTaskId(), taskJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    RetrieveTaskResponse res = new RetrieveTaskResponse(taskObj);
    return res;
  }

}

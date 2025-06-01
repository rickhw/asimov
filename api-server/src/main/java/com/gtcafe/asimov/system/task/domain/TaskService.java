package com.gtcafe.asimov.system.task.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.hello.consumer.HelloEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {

  @Autowired
  private CacheRepository _repos;

  @Autowired
  private JsonUtils _jsonUtils;

  // public TaskDomainObject retrieve(String id) {
  //   // 1. validate: is not exist or expire.

  //   // 2. find the id in cache
  //   String jsonString = _repos.retrieveObject(id);
  //   TaskDomainObject tdo = jsonUtils.jsonStringToModel(jsonString, TaskDomainObject.class);

  //   logger.info("TaskObject: {}", jsonString);

  //   return tdo;
  // }

  public HelloEvent retrieveV4(String id) {
    // 1. validate: is not exist or expire.
    // log.info("id: [{}]", id);

    String cacheKey = String.format("sys.Task:%s", id);

    // 2. find the id in cache
    String jsonString = _repos.retrieveObject(cacheKey).get();
    HelloEvent event = _jsonUtils.jsonStringToModelSafe(jsonString, HelloEvent.class).get();
    // log.info("SayHelloEventV4: [{}]", jsonString);

    // 3. find the id in db

    return event;
  }

}

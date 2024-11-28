package com.gtcafe.asimov.apiserver.platform.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.common.utils.JsonUtils;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.system.cache.CacheRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {

  @Autowired
  CacheRepository _repos;

  @Autowired
  TaskMapper _dto;

  @Autowired
  private JsonUtils jsonUtils;

  // public TaskDomainObject retrieve(String id) {
  //   // 1. validate: is not exist or expire.

  //   // 2. find the id in cache
  //   String jsonString = _repos.retrieveObject(id);
  //   TaskDomainObject tdo = jsonUtils.jsonStringToModel(jsonString, TaskDomainObject.class);

  //   logger.info("TaskObject: {}", jsonString);

  //   return tdo;
  // }

  public SayHelloEvent retrieveV4(String id) {
    // 1. validate: is not exist or expire.

    // 2. find the id in cache
    String jsonString = _repos.retrieveObject(id);
    SayHelloEvent tdo = jsonUtils.jsonStringToModel(jsonString, SayHelloEvent.class);

    log.info("SayHelloEventV4: {}", jsonString);

    return tdo;
  }

}

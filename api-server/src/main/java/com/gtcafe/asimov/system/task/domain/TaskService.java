package com.gtcafe.asimov.system.task.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {

  @Autowired
  CacheRepository _repos;

  // @Autowired
  // TaskMapper _dto;

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

  // public HelloEvent retrieveV4(String id) {
  //   // 1. validate: is not exist or expire.

  //   // 2. find the id in cache
  //   String jsonString = _repos.retrieveObject(id);
  //   HelloEvent tdo = jsonUtils.jsonStringToModel(jsonString, HelloEvent.class);

  //   log.info("SayHelloEventV4: {}", jsonString);

  //   return tdo;
  // }

}

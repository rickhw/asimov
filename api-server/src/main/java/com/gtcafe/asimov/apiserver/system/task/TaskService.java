package com.gtcafe.asimov.apiserver.system.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.utils.JsonUtils;

@Service
public class TaskService {
  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  @Autowired
  CacheRepository _repos;

  @Autowired
  TaskDataTransferObject _dto;

  @Autowired
  private JsonUtils jsonUtils;

  public TaskDomainObject retrieve(String id) {
    // 1. validate: is not exist or expire.

    // 2. find the id in cache
    String jsonString = _repos.retrieveObject(id);
    TaskDomainObject tdo = jsonUtils.jsonStringToModel(jsonString, TaskDomainObject.class);

    logger.info("TaskObject: {}", jsonString);

    return tdo;
  }


}

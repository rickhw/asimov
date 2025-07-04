package com.gtcafe.asimov.system.task.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.hello.consumer.HelloEvent;
import com.gtcafe.asimov.system.task.TaskUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {

  @Autowired
  private CacheRepository _cacheRepos;

  @Autowired
  private JsonUtils _jsonUtils;

  public HelloEvent retrieve(String id) {
    String cacheKey = TaskUtils.renderCacheKey(id); //String.format("sys.Task:%s", id);

    // 2. find the id in cache
    String jsonString = _cacheRepos.retrieveObject(cacheKey).get();
    HelloEvent event = _jsonUtils.jsonStringToModelSafe(jsonString, HelloEvent.class).get();

    // 3. find the id in db

    return event;
  }

}

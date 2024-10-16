package com.gtcafe.asimov.consumer.system.task;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.system.task.TaskState;
import com.gtcafe.asimov.core.utils.JsonUtils;

@Service
@Slf4j
public class TaskEventHandler {

    @Autowired
    private JsonUtils _jsonUtils;

    @Autowired
    CacheRepository _cacheRepos;

    public void transit(TaskDomainObject tdo, TaskState toState) {
        // 1. update to cache
        System.out.printf("Update task: Id: [%s]\n", tdo.getTaskId());

        tdo.getMetadata().set_state(toState);

        // 2. store task to cache
        String taskJsonString = _jsonUtils.modelToJsonString(tdo);
        _cacheRepos.saveOrUpdateObject(tdo.getTaskId(), taskJsonString);

        // 3. store task to database

        // 4. log the status
        log.info("TaskID: [{}], State: [{}], Spec: [{}]", tdo.getTaskId(), tdo.getMetadata().get_state(), tdo.getSpec());

        System.out.printf("Update status from PENDING to RUNNING");

    }
}
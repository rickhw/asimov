package com.gtcafe.asimov.consumer.system.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.system.task.TaskState;
import com.gtcafe.asimov.core.utils.JsonUtils;

@Service
public class TaskEventHandler {

    @Autowired
    private JsonUtils _jsonUtils;

    @Autowired
    CacheRepository _cacheRepos;

    public void transit(TaskDomainObject tdo, TaskState toState) {
        // 1. update to cache
        System.out.printf("Update task: Id: [%s]", tdo.getTaskId());

        tdo.getMetadata().set_state(toState);

        // 2. store task to cache
        String taskJsonString = _jsonUtils.modelToJsonString(tdo);
        _cacheRepos.saveOrUpdateObject(tdo.getTaskId(), taskJsonString);

        // 3. store task to database
                 
        System.out.printf("TaskID: [%s]\n", tdo.getTaskId());
        System.out.printf("State: [%s]\n", tdo.getMetadata().get_state());
        System.out.printf("Spec: [%s]\n", tdo.getSpec());

        System.out.printf("Update status from PENDING to RUNNING");

    }
}
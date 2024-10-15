package com.gtcafe.asimov.consumer.system.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.utils.JsonUtils;

@Service
public class TaskEventHandler {

    @Autowired
    private JsonUtils _JsonUtils;

    public void handle(String message) {
        TaskDomainObject tdo = _JsonUtils.jsonStringToModel(message, TaskDomainObject.class);
         
        System.out.printf("TaskID: [%s]\n", tdo.getTaskId());
        System.out.printf("State: [%s]\n", tdo.getMetadata().get_state());
        System.out.printf("Spec: [%s]\n", tdo.getSpec());
    }
}
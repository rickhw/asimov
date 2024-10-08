package com.gtcafe.asimov.consumer.system.task;

import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.system.task.TaskDomainObject;

@Service
public class TaskEventHandler {

    public void handle(TaskDomainObject tdo) {
        System.out.printf("TaskID: [%s]\n", tdo.getTaskId());
        System.out.printf("State: [%s]\n", tdo.getMetadata().get_state());
        System.out.printf("Spec: [%s]\n", tdo.getSpec());
    }
}
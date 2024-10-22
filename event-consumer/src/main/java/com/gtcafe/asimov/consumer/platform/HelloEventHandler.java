package com.gtcafe.asimov.consumer.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.platform.hello.SayHelloEventV4;
import com.gtcafe.asimov.core.platform.hello.SayHelloMessage;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.system.task.TaskState;
import com.gtcafe.asimov.core.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloEventHandler {

    @Autowired
    private JsonUtils _jsonUtils;

    @Autowired
    CacheRepository _cacheRepos;


    public void sayHello(SayHelloMessage message, TaskDomainObject tdo) {

        try {
            Thread.sleep(10000);
            System.out.println("message: " + message.getData());

            // send event.

            // sequence
            // 1. Change Task State from PENDING to RUNNING
            // TaskDomainObject tdo = event.getTask();
            tdo.getMetadata().set_state(TaskState.COMPLETED);

            // 1.1 Update to cache
            String taskJsonString = _jsonUtils.modelToJsonString(tdo);
            _cacheRepos.saveOrUpdateObject(tdo.getTaskId(), taskJsonString);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sayHelloV4(SayHelloEventV4 event) {

        try {
            Thread.sleep(10000);
            System.out.println("message: " + event.getData());

            // TaskDomainObject tdo = event.getTask();
            event.transit(TaskState.COMPLETED);

            // 1.1 Update to cache
            String taskJsonString = _jsonUtils.modelToJsonString(event);
            _cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
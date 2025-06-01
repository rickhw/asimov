package com.gtcafe.asimov.system.hello.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.constants.QueueName;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.task.domain.TaskService;
import com.gtcafe.asimov.system.task.schema.TaskState;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloConsumer {

    @Autowired
    private JsonUtils _jsonUtils;

    @Autowired
    private CacheRepository _cacheRepos;

    @Autowired
    private HelloEventHandler _eventHandler;

    @Autowired
    private TaskService _taskService;


    @Async(value = "helloThreadPool")
    @RabbitListener(queues = QueueName.HELLO_QUEUE, autoStartup = "false")
    public void consumeHelloQueue(String eventString) {
        // log.info("Received message: [{}]", eventString);

        // 1. handle data model: convert json string to model
        HelloEvent event = _jsonUtils.jsonStringToModelSafe(eventString, HelloEvent.class).get();
        log.info("task: [{}], state: [{}] ...: ", event.getId(), event.getState());

        // 2. update state: update state from pending to running
        String cachedKey = String.format("%s:%s", KindConstants.PLATFORM_HELLO, event.getId());
        event.setState(TaskState.RUNNING);
        // log.info("start the conusmer, cachedKey: [{}], state: [{}]", cachedKey, event.getState());
        
        // 2.1: persist state to cache
        String afterEventString = _jsonUtils.modelToJsonStringSafe(event).get();
        _cacheRepos.saveOrUpdateObject(cachedKey, afterEventString);

        // 2.2: persist state to db

        // start processing (running)
        _eventHandler.handleEvent(event);
        
    }
}


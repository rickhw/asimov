package com.gtcafe.asimov.system.hello.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.constants.QueueName;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
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

    @RabbitListener(queues = QueueName.HELLO_QUEUE, autoStartup = "false")
    public void consumeHelloQueue(String eventString) {
        // log.info("Received message: [{}]", eventString);

        // convert json string to model
        HelloEvent event = _jsonUtils.jsonStringToModel(eventString, HelloEvent.class);
        String cachedKey = String.format("%s:%s", KindConstants.PLATFORM_HELLO, event.getId());

        // log.info("start the conusmer, cachedKey: [{}], state: [{}]", cachedKey, event.getState());
        log.info("task: [{}], state: [{}], data: [{}] ...: ", event.getId(), event.getState(), event.getData());

        event.setState(TaskState.RUNNING);

        // update state from pending to running
        String afterEventString = _jsonUtils.modelToJsonString(event);
        _cacheRepos.saveOrUpdateObject(cachedKey, afterEventString);

        // start processing (running)
        _eventHandler.handleEvent(event);
        
    }
}


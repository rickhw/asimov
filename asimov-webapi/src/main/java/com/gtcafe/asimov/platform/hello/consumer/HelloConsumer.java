package com.gtcafe.asimov.platform.hello.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.system.cache.CacheRepository;
import com.gtcafe.asimov.system.constants.QueueName;
import com.gtcafe.asimov.system.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloConsumer {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private CacheRepository cacheRepos;

    @Autowired
    private HelloEventHandler eventHandler;

    @RabbitListener(queues = QueueName.HELLO_QUEUE)
    public void consumeHelloQueue(String eventString) {

        // convert json string to model
        HelloEvent event = jsonUtils.jsonStringToModel(eventString, HelloEvent.class);

        // 
        cacheRepos.saveOrUpdateObject(event.getEventId(), eventString);

        eventHandler.handleEvent(event);
        // 根據 data 類型取得相應的處理器並執行處理邏輯
        // SayHelloMessage data = event.getData();
        // EventHandler<T> handler = eventHandlerRegistry.getHandler(data.getClass());
        // if (handler != null) {
        //     handler.handleEvent(event);
        // } else {
        //     log.warn("No handler found for event type: {}", data.getClass());
        // }
    }
}


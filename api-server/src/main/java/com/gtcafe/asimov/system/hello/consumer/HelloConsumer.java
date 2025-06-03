package com.gtcafe.asimov.system.hello.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.constants.QueueName;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.task.domain.TaskService;
import com.gtcafe.asimov.system.task.schema.TaskState;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloConsumer {

    @Autowired
    private JsonUtils _jsonUtils;

    @Autowired
    private CacheRepository _cacheRepos;

    @Autowired
    private HelloEventHandler _handler;

    @Autowired
    private TaskService _taskService;


    @Async(value = "helloThreadPool")
    // @RabbitListener(queues = QueueName.HELLO_QUEUE, autoStartup = "false")
    @RabbitListener(
        //queues = "${application.rabbitmq.queue-name}",
        queues = QueueName.HELLO_QUEUE,
        containerFactory = "rabbitListenerContainerFactory"
    )
    // public void consumeHelloQueue(String eventString) {
    public void receiveMessage(
        String eventString,
        Message message, 
        Channel channel, 
        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
    ) throws IOException {
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
        try {
            boolean processed = _handler.handleEvent(event);
            
            if (processed) {
                // 手動確認消息
                channel.basicAck(deliveryTag, false);
                // log.info("Message processed successfully: {}", messageBody);
            } else {
                // 處理失敗，重新入隊
                channel.basicNack(deliveryTag, false, true);
                // log.warn("Message processing failed, will be requeued: {}", messageBody);
            }
        } catch (Exception e) {
            // 發生異常，重新入隊
            channel.basicNack(deliveryTag, false, true);
            // log.error("Unexpected error processing message", e);
        }
        
    }
}


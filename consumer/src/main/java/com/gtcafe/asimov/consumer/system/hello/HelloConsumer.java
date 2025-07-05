package com.gtcafe.asimov.consumer.system.hello;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.infrastructure.queue.QueueMdcUtils;
import com.gtcafe.asimov.system.hello.HelloConstants;
import com.gtcafe.asimov.system.hello.HelloUtils;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfig;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
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

    // @Autowired
    // private HelloUtils _utils;

    @Autowired
    private HelloQueueConfig _qconfig;

    @Autowired
    private TaskService _taskService;

    @Async(value = HelloConstants.THREAD_POOL_EXECUTOR_BEANNAME)
    // @RabbitListener(queues = QueueName.HELLO_QUEUE, autoStartup = "false")
    @RabbitListener(
        //queues = "${application.rabbitmq.queue-name}",
        // queues = QueueName.HELLO_QUEUE,
        queues = "${asimov.system.hello.queues.task-queue.queue-name}",
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
        // log dequeue
        QueueMdcUtils.Dequeue(_qconfig.getQueueName(), _qconfig.getExchangeName(), _qconfig.getRoutingKeyName());

        // 1. handle data model: convert json string to model
        HelloEvent event = _jsonUtils.jsonStringToModelSafe(eventString, HelloEvent.class).get();
        log.info("task: [{}], state: [{}] ...: ", event.getId(), event.getState());

        // 2. update state: update state from pending to running
        String cachedKey = HelloUtils.renderCacheKey(event.getId());

        event.setState(TaskState.RUNNING);
        
        // 2.1: persist state to cache
        String afterEventString = _jsonUtils.modelToJsonStringSafe(event).get();
        _cacheRepos.saveOrUpdateObject(cachedKey, afterEventString);

        // 2.2: persist state to db
        // @TODO

        // start processing (running)
        try {
            boolean processed = _handler.handleEvent(event);
            
            if (processed) {
                channel.basicAck(deliveryTag, false); // 手動確認
            } else {
                channel.basicNack(deliveryTag, false, true); // 處理失敗，重新入隊
            }
        } catch (Exception e) {
            
            channel.basicNack(deliveryTag, false, true); // 發生異常，重新入隊
            QueueMdcUtils.Requeue(_qconfig.getQueueName(), _qconfig.getExchangeName(), _qconfig.getRoutingKeyName());
           
            log.error(String.format("發生異常，Message 重新排隊, cachedKey: [%s]", cachedKey), e);
        }
        
    }
}


package com.gtcafe.asimov.system.queue;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.task.schema.Task;
import com.gtcafe.asimov.system.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private RabbitInitializer rabbitInitizlizer;

    @Autowired
    private JsonUtils jsonUtils;

    public <T extends Task<?>> T sendEvent(T event, String queueName) {
        QueueConfig queueConfig = rabbitInitizlizer.getQueueConfig(queueName);

        if (queueConfig == null) {
            log.error("Queue config not found for [{}]", queueName);
            return null;
        }

        String eventJsonString = jsonUtils.modelToJsonString(event);
        rabbitTemplate.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), eventJsonString);

        log.info("Pushed message to queue: [{}], exchange: [{}], routingKey: [{}]", queueConfig.getName(), queueConfig.getExchange(), queueConfig.getRoutingKey());
        return event;
    }
}


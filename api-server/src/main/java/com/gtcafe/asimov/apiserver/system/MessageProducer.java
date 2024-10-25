package com.gtcafe.asimov.apiserver.system;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.system.config.RabbitConfig;
import com.gtcafe.asimov.core.model.RabbitQueueConfig;
import com.gtcafe.asimov.core.system.event.Event;
import com.gtcafe.asimov.core.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private RabbitConfig rabbitConfig;

    @Autowired
    private JsonUtils jsonUtils;

    public <T extends Event<?>> T sendEvent(T event, String queueName) {
        RabbitQueueConfig queueConfig = rabbitConfig.getQueueConfig(queueName);

        if (queueConfig == null) {
            log.error("Queue config not found for {}", queueName);
            return null;
        }

        String eventJsonString = jsonUtils.modelToJsonString(event);
        rabbitTemplate.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), eventJsonString);

        log.info("Pushed message to queue: [{}], exchange: [{}], routingKey: [{}]", queueConfig.getName(), queueConfig.getExchange(), queueConfig.getRoutingKey());
        return event;
    }
}


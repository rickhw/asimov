package com.gtcafe.asimov.infrastructure.queue;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.queue.model.QueueConfig;
import com.gtcafe.asimov.system.task.schema.Task;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Producer {

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

        QueueMdcUtils.Enqueue(queueName, queueName, queueName);
        log.info("Pushed message to queue: [{}], exchange: [{}], routingKey: [{}]", queueConfig.getName(), queueConfig.getExchange(), queueConfig.getRoutingKey());

        String eventJsonString = jsonUtils.modelToJsonStringSafe(event).get();
        rabbitTemplate.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), eventJsonString);

        return event;
    }
}


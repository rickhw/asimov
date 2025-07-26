package com.gtcafe.asimov.consumer.system.hello;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfigService;
import com.gtcafe.asimov.system.hello.model.HelloEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HelloConsumer {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private HelloEventHandler helloEventHandler;

    @Autowired
    private HelloQueueConfigService queueConfigService;

    @RabbitListener(queues = "#{helloQueueConfigService.queueName}")
    public void receiveMessage(@Payload String eventString) {
        log.info("Received message on main queue: {}", eventString);

        Optional<HelloEvent> eventOptional = jsonUtils.jsonStringToModelSafe(eventString, HelloEvent.class);

        if (eventOptional.isEmpty()) {
            log.error("Failed to deserialize message to HelloEvent: {}", eventString);
            // If deserialization fails, we treat it as a permanent failure and send to DLQ
            throw new RuntimeException("Deserialization failed for message: " + eventString);
        }

        HelloEvent event = eventOptional.get();

        try {
            boolean processed = helloEventHandler.handleEvent(event);
            if (!processed) {
                // If handleEvent returns false, it means the business logic failed
                // We re-throw to trigger the DLQ mechanism
                throw new RuntimeException("Business logic failed for event: " + event.getId());
            }
            log.info("Processed event successfully: {}", event.getId());
        } catch (Exception e) {
            log.error("Error processing event: {}", event.getId(), e);
            // Re-throw the exception to trigger the DLQ mechanism
            throw e;
        }
    }

    @RabbitListener(queues = "#{helloQueueConfigService.deadLetterQueueName}")
    public void receiveDeadLetterMessage(@Payload String eventString) {
        log.error("Received dead-lettered message on queue [{}]: {}. Storing for analysis.", 
            queueConfigService.getDeadLetterQueueName(), eventString);
        // Here you can add logic to handle the failed message,
        // e.g., save it to a database, send a notification, etc.
    }
}

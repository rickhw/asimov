package com.gtcafe.asimov.consumer.system.hello;

import com.gtcafe.asimov.consumer.system.hello.config.HelloConsumerAsyncConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HelloConsumer {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumer.class);

    @RabbitListener(queues = HelloConsumerAsyncConfig.QUEUE_NAME)
    public void receiveMessage(@Payload Map<String, String> payload) {
        logger.info("Received message on main queue: {}", payload);

        try {
            // Simulate processing
            if (payload != null && "fail".equals(payload.get("name"))) {
                logger.warn("Simulating failure for message: {}", payload);
                throw new RuntimeException("Simulated failure for 'fail' message");
            }
            logger.info("Processed message successfully: {}", payload);
        } catch (Exception e) {
            logger.error("Error processing message: {}", payload, e);
            // Re-throw the exception to trigger the DLQ mechanism
            throw e;
        }
    }

    @RabbitListener(queues = HelloConsumerAsyncConfig.QUEUE_NAME + HelloConsumerAsyncConfig.DLQ_SUFFIX)
    public void receiveDeadLetterMessage(@Payload Map<String, String> payload) {
        logger.error("Received dead-lettered message: {}. Storing for analysis.", payload);
        // Here you can add logic to handle the failed message,
        // e.g., save it to a database, send a notification, etc.
    }
}
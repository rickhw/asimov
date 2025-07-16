package com.gtcafe.asimov.consumer.system.hello.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConsumerAsyncConfig {

    public static final String EXCHANGE_NAME = "hello.async.ex";
    public static final String QUEUE_NAME = "hello.async.q";
    public static final String ROUTING_KEY = "hello.async.rk";

    private static final String DLX_SUFFIX = ".dlx";
    public static final String DLQ_SUFFIX = ".dlq";

    // Main Queue
    @Bean
    Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", EXCHANGE_NAME + DLX_SUFFIX)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY) // Route with the original routing key
                .build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    // Dead Letter Queue
    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(EXCHANGE_NAME + DLX_SUFFIX);
    }

    @Bean
    Queue deadLetterQueue() {
        return new Queue(QUEUE_NAME + DLQ_SUFFIX, true);
    }

    @Bean
    Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(ROUTING_KEY);
    }
}
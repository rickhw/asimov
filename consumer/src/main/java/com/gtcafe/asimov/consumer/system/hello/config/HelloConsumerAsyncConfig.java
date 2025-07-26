package com.gtcafe.asimov.consumer.system.hello.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gtcafe.asimov.system.hello.config.HelloQueueConfigService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class HelloConsumerAsyncConfig {

    @Autowired
    private HelloQueueConfigService queueConfigService;

    // Main Queue
    @Bean
    Queue helloQueue() {
        // 驗證配置
        if (!queueConfigService.validateConfig()) {
            throw new IllegalStateException("Hello queue configuration is invalid");
        }
        
        log.info("Creating hello queue with config: {}", queueConfigService.getConfigSummary());
        
        return QueueBuilder.durable(queueConfigService.getQueueName())
                .withArgument("x-dead-letter-exchange", queueConfigService.getDeadLetterExchangeName())
                .withArgument("x-dead-letter-routing-key", queueConfigService.getRoutingKeyName())
                .build();
    }

    @Bean
    DirectExchange helloExchange() {
        return new DirectExchange(queueConfigService.getExchangeName());
    }

    @Bean
    Binding helloBinding(Queue helloQueue, DirectExchange helloExchange) {
        return BindingBuilder.bind(helloQueue).to(helloExchange).with(queueConfigService.getRoutingKeyName());
    }

    // Dead Letter Queue
    @Bean
    DirectExchange helloDeadLetterExchange() {
        return new DirectExchange(queueConfigService.getDeadLetterExchangeName());
    }

    @Bean
    Queue helloDeadLetterQueue() {
        return new Queue(queueConfigService.getDeadLetterQueueName(), true);
    }

    @Bean
    Binding helloDeadLetterBinding(Queue helloDeadLetterQueue, DirectExchange helloDeadLetterExchange) {
        return BindingBuilder.bind(helloDeadLetterQueue).to(helloDeadLetterExchange).with(queueConfigService.getRoutingKeyName());
    }

    /**
     * 獲取佇列名稱 (向後相容)
     * @deprecated 請使用 HelloQueueConfigService.getQueueName()
     */
    @Deprecated
    public String getQueueName() {
        return queueConfigService.getQueueName();
    }

    /**
     * 獲取 DLQ 名稱 (向後相容)
     * @deprecated 請使用 HelloQueueConfigService.getDeadLetterQueueName()
     */
    @Deprecated
    public String getDeadLetterQueueName() {
        return queueConfigService.getDeadLetterQueueName();
    }
}
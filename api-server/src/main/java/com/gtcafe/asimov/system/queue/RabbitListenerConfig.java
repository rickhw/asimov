package com.gtcafe.asimov.system.queue;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/** 
 * 控制 Consumer 啟動次序，避免 RabbitAdmin 還沒有完成初始化，
 * Consumer 的 RabbitMQ Listener 就開始執行.
 * RabbitListener 需要指定 autoStartup = "false"，才能使用此設定.
 **/
@Configuration
@Slf4j
public class RabbitListenerConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public CommandLineRunner startListeners(RabbitListenerEndpointRegistry registry) {
        return args -> {
            log.info("Starting all of RabbitListeners ...");
            registry.start();
        };
    }
}
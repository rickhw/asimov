package com.gtcafe.asimov.consumer.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConsumerConfig implements RabbitListenerConfigurer {

    // 定義一個 Listener 的 Container 工廠，並使用 JSON 消息轉換器
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
            // Jackson2JsonMessageConverter converter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // factory.setMessageConverter(converter);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);  // 自動確認

        return factory;
    }

    // @Bean
    // public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        
    //     SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    //     container.setConnectionFactory(connectionFactory);
    //     container.setAcknowledgeMode(AcknowledgeMode.AUTO); // 設定自動確認

    //     return container;
    // }


	// // convert the java object to json string.
	// @Bean
	// public Jackson2JsonMessageConverter jsonMessageConverter() {
	// 	return new Jackson2JsonMessageConverter();
	// }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        // 可以在這裡進行進階配置
    }
}
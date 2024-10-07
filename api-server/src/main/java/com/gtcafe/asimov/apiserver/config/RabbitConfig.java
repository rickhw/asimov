package com.gtcafe.asimov.apiserver.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.gtcafe.asimov.core.model.RabbitQueueConfig;

@Configuration
@ConfigurationProperties(prefix = "app.rabbitmq")
public class RabbitConfig {

	private static final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
	private List<RabbitQueueConfig> queues;
	private Map<String, RabbitQueueConfig> queueMap = new HashMap<>();

	// 初始化时将队列配置存储到 Map，使用 @PostConstruct
	// @PostConstruct
	// public void initQueueMap() {
	// queueMap =
	// queues.stream().collect(Collectors.toMap(RabbitQueueConfig::getName,
	// queueConfig -> queueConfig));
	// }

	// 改为 EventListener，等待 Spring 上下文初始化完成后再执行
	@EventListener(ContextRefreshedEvent.class)
	public void initQueueMap() {
		queueMap = queues.stream().collect(Collectors.toMap(RabbitQueueConfig::getName, queueConfig -> queueConfig));
	}

	// 获取队列配置
	public RabbitQueueConfig getQueueConfig(String queueName) {
		return queueMap.get(queueName);
	}

	public List<RabbitQueueConfig> getQueues() {
		return queues;
	}

	public void setQueues(List<RabbitQueueConfig> queues) {
		this.queues = queues;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	// convert the java object to json string.
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public Declarables rabbitMQDeclarables() {
		List<Declarable> declarablesList = queues.stream().map(queueConfig -> {
			logger.info("Init RabbitMQ config: queueName: [{}], exchange: [{}], routingKey: [{}]", 
					queueConfig.getName(), queueConfig.getExchange(), queueConfig.getRoutingKey());

			Queue queue = new Queue(queueConfig.getName(), true);

			Exchange exchange = "fanoutExchange".equals(queueConfig.getExchange())
					? new FanoutExchange(queueConfig.getExchange())
					: new DirectExchange(queueConfig.getExchange());

			Binding binding = exchange instanceof FanoutExchange
					? BindingBuilder.bind(queue).to((FanoutExchange) exchange)
					: BindingBuilder.bind(queue).to((DirectExchange) exchange).with(queueConfig.getRoutingKey());

			return new Declarables(queue, exchange, binding);
		}).flatMap(declarables -> declarables.getDeclarables().stream()).collect(Collectors.toList());

		return new Declarables(declarablesList);
	}
}

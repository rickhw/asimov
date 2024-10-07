package com.gtcafe.asimov.apiserver.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.model.RabbitQueueConfig;
import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.IMessage;

import com.gtcafe.asimov.apiserver.config.RabbitConfig;
import com.gtcafe.asimov.apiserver.platform.task.TaskConstants;
import com.gtcafe.asimov.apiserver.platform.task.pojo.TaskDomainObject;

@Service
public class MessageProducer {

	private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

	@Autowired
	private AmqpTemplate rabbitTemplate;

		@Autowired
	private RabbitConfig rabbitConfig;

	public void sendEvent(String queueName, Event<? extends IMessage> event) {
		rabbitTemplate.convertAndSend(queueName, event);
	}

	public Event<IMessage> sendEvent(String queneName, EventType type, IMessage message) {

		Event<IMessage> event = new Event<>(type, message);

		// Get RabbitMQ config for the "platform.sayHello" queue
		RabbitQueueConfig queueConfig = rabbitConfig.getQueueConfig(queneName);

		// TODO: add exception handler: HTTP 500 internal error
		if (queueConfig == null) {
			logger.error("Queue config not found for platform.sayHello");
			return null;
		}

		// Send event to the queue using dynamic exchange and routing key
		rabbitTemplate.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), event);

		logger.info("Push message to queue: [{}], exchange: [{}], routingKey: [{}]", queueConfig.getName(), queueConfig.getExchange(), queueConfig.getRoutingKey());

		return event;
	}

	public TaskDomainObject sendTaskEvent(TaskDomainObject tdo) {

		// Event<IMessage> event = new Event<>(type, message);

		// Get RabbitMQ config for the "platform.sayHello" queue
		RabbitQueueConfig queueConfig = rabbitConfig.getQueueConfig(TaskConstants.TASK_QUEUE_NAME);

		// TODO: add exception handler: HTTP 500 internal error
		if (queueConfig == null) {
			logger.error("Queue config not found for platform.sayHello");
			return null;
		}

		// Send event to the queue using dynamic exchange and routing key
		rabbitTemplate.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), tdo);

		logger.info("Push message to queue: [{}], exchange: [{}], routingKey: [{}]", queueConfig.getName(), queueConfig.getExchange(), queueConfig.getRoutingKey());

		return tdo;

	}


}

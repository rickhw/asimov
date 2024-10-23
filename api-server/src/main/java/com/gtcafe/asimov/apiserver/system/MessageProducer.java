package com.gtcafe.asimov.apiserver.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.system.config.RabbitConfig;
import com.gtcafe.asimov.core.constants.QueueName;
import com.gtcafe.asimov.core.model.RabbitQueueConfig;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.platform.tenant.RegisterTenantEvent;
import com.gtcafe.asimov.core.utils.JsonUtils;


@Service
public class MessageProducer {

	private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Autowired
	private RabbitConfig rabbitConfig;

	@Autowired
  	private JsonUtils jsonUtils;

	public SayHelloEvent sendSayHelloEvent(SayHelloEvent event, String queneName) {

		// Get RabbitMQ config for the "platform.sayHello" queue
		RabbitQueueConfig queueConfig = rabbitConfig.getQueueConfig(queneName);

		// TODO: add exception handler: HTTP 500 internal error
		if (queueConfig == null) {
			logger.error("Queue config not found for platform.sayHello");
			return null;
		}

		String eventJsonString = jsonUtils.modelToJsonString(event);
		// Send event to the queue using dynamic exchange and routing key
		rabbitTemplate.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), eventJsonString);

		logger.info("Push message to queue: [{}], exchange: [{}], routingKey: [{}]", queueConfig.getName(), queueConfig.getExchange(), queueConfig.getRoutingKey());
		return event;

	}

	public RegisterTenantEvent sendRegisterTenantEvent(RegisterTenantEvent event) {

		// Get RabbitMQ config for the "platform.sayHello" queue
		RabbitQueueConfig queueConfig = rabbitConfig.getQueueConfig(QueueName.REGISTER_TENANT_QUEUE_NAME);

		// TODO: add exception handler: HTTP 500 internal error
		if (queueConfig == null) {
			logger.error("Queue config not found for platform.sayHello");
			return null;
		}

		String eventJsonString = jsonUtils.modelToJsonString(event);
		// Send event to the queue using dynamic exchange and routing key
		rabbitTemplate.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), eventJsonString);

		logger.info("Push message to queue: [{}], exchange: [{}], routingKey: [{}]", queueConfig.getName(), queueConfig.getExchange(), queueConfig.getRoutingKey());
		return event;

	}
}

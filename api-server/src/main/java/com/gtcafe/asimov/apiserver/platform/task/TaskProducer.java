package com.gtcafe.asimov.apiserver.platform.task;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.IMessage;


import java.util.UUID;

@Service
public class TaskProducer {

    @Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${app.rabbitmq.exchange}")
	private String exchange;

	@Value("${app.rabbitmq.routingkey}")
	private String routingkey;

	// private final static String CREATE_TENANT_QUEUE = "create-tenant";

	public void sendEvent(String queueName, Event<? extends IMessage> event) {
		rabbitTemplate.convertAndSend(queueName, event);
	}

	// public void sendCreateTenantEvent(Event<? extends IMessage> event) {
	// 	rabbitTemplate.convertAndSend(CREATE_TENANT_QUEUE, event);
	// }


}

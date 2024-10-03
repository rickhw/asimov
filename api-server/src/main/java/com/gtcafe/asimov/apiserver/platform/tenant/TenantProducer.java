package com.gtcafe.asimov.apiserver.platform.tenant;

import java.util.UUID;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.IMessage;

import com.gtcafe.asimov.core.platform.tenant.*;

@Service
public class TenantProducer {

    @Autowired
	private AmqpTemplate rabbitTemplate;

	// @Value("${app.rabbitmq.exchange}")
	// private String exchange;

	// @Value("${app.rabbitmq.routingkey}")
	// private String routingkey;

	public void sendEvent(String queueName, Event<? extends IMessage> event) {
		// rabbitTemplate.convertAndSend(queueName, "eventRoutingKey", event);
		rabbitTemplate.convertAndSend(queueName, event);
		System.out.println("Event Sent: " + event.getEventType());
	}

	public Event<CreateTenantMessage> sentRegisterEvent(CreateTenantMessage message) {
		Event<CreateTenantMessage> event = new Event<>(EventType.REGISTER_TENANT, message);
		sendEvent(TenantConstants.REGISTER_TENANT_QUEUE_NAME, event);

		return event;
	}

	public Event<DeleteTenantMessage> sendDeregisterEvent(String id) {
		DeleteTenantMessage message = new DeleteTenantMessage(id);
		Event<DeleteTenantMessage> event = new Event<>(EventType.DEREGISTER_TENANT, message);
		sendEvent(TenantConstants.DEREGISTER_TENANT_QUEUE_NAME, event);

		return event;
	}
}

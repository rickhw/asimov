package com.gtcafe.asimov.apiserver.platform.tenant;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.IMessage;
import com.gtcafe.asimov.core.platform.tenant.*;


import java.util.UUID;


@Service
public class TenantProducer {

    @Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${app.rabbitmq.exchange}")
	private String exchange;

	@Value("${app.rabbitmq.routingkey}")
	private String routingkey;

	public void sendEvent(Event<? extends IMessage> event) {
		rabbitTemplate.convertAndSend("eventExchange", "eventRoutingKey", event);
		System.out.println("Event Sent: " + event.getEventType());
	}

	public Event<CreateTenantMessage> sentCreateEvent(CreateTenantMessage message) {
		Event<CreateTenantMessage> event = new Event<>(EventType.CREATE_CONTAINER, message);
		sendEvent(event);

		return event;
	}

	public Event<DeleteTenantMessage> sendDeleteEvent(String containerId) {
		DeleteTenantMessage message = new DeleteTenantMessage(containerId);
		Event<DeleteTenantMessage> event = new Event<>(EventType.DELETE_CONTAINER, message);
		sendEvent(event);

		return event;
	}
}

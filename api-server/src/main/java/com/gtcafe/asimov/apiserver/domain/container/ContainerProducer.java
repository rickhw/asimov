package com.gtcafe.asimov.apiserver.domain.container;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.IMessage;
import com.gtcafe.asimov.core.domain.container.*;

import java.util.UUID;


@Service
public class ContainerProducer {

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

	public Event<CreateContainerMessage> sendCreateContainerEvent(CreateContainerMessage message) {
		Event<CreateContainerMessage> event = new Event<>(EventType.CREATE_CONTAINER, message);
		sendEvent(event);

		return event;
	}

	public Event<DeleteContainerMessage> sendDeleteContainerMessageEvent(String containerId) {
		DeleteContainerMessage message = new DeleteContainerMessage(containerId);
		Event<DeleteContainerMessage> event = new Event<>(EventType.DELETE_CONTAINER, message);
		sendEvent(event);

		return event;
	}
}

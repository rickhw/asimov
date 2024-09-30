package com.gtcafe.asimov.apiserver.services;

import com.gtcafe.asimov.core.event.EventType;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.IMessage;
import com.gtcafe.asimov.core.event.message.CreateContainerMessage;
import com.gtcafe.asimov.core.event.message.DeleteContainerMessage;

import java.util.UUID;


@Service
public class MessageProducer {

    @Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${app.rabbitmq.exchange}")
	private String exchange;

	@Value("${app.rabbitmq.routingkey}")
	private String routingkey;

//	public void send(Event event) {
//		rabbitTemplate.convertAndSend(exchange, routingkey, event);
//		System.out.println("Enqueue, event: " + event);
//	}

	// 发送 Event 消息
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

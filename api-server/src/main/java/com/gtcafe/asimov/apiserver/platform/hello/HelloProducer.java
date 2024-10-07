package com.gtcafe.asimov.apiserver.platform.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.platform.SayHelloMessage;

@Service
public class HelloProducer {
	private static final Logger logger = LoggerFactory.getLogger(HelloProducer.class);

    @Autowired
	private AmqpTemplate rabbitTemplate;

	public Event<SayHelloMessage> sentSayHelloEvent(SayHelloMessage message) {
		// assemble message and event
		Event<SayHelloMessage> event = new Event<>(EventType.SAY_HELLO, message);

		// sent event to queue
		rabbitTemplate.convertAndSend("directExchange", "routingKey2", event);
		// sendEvent(HelloConstants.SAY_HELLO_QUEUE_NAME, event);

		logger.info("Push message to queue: [{}], routingKey: [routingKey2]", HelloConstants.SAY_HELLO_QUEUE_NAME);
		
		return event;
	}

}

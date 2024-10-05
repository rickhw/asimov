package com.gtcafe.asimov.apiserver.platform.hello;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.platform.SayHelloMessage;

@Service
public class HelloProducer {

    @Autowired
	private AmqpTemplate rabbitTemplate;

	public Event<SayHelloMessage> sentSayHelloEvent(SayHelloMessage message) {
		// assemble message and event
		Event<SayHelloMessage> event = new Event<>(EventType.SAY_HELLO, message);

		// sent event to queue
		rabbitTemplate.convertAndSend(HelloConstants.SAY_HELLO_QUEUE_NAME, event);
		// sendEvent(HelloConstants.SAY_HELLO_QUEUE_NAME, event);

		return event;
	}

}

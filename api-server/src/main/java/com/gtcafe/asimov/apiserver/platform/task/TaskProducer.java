package com.gtcafe.asimov.apiserver.platform.task;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.IMessage;

@Service
public class TaskProducer {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void sendEvent(String queueName, Event<? extends IMessage> event) {
		rabbitTemplate.convertAndSend(queueName, event);
	}

	public void sendEvent(IMessage message) {

		Event<IMessage> event = new Event<>(EventType.ASYNC_TASK, message);
		rabbitTemplate.convertAndSend(TaskConstants.TASK_QUEUE_NAME, event);

	}

}

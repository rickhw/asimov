package com.gtcafe.asimov.apiserver.platform.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.IMessage;

@Service
public class TaskProducer {

	private static final Logger logger = LoggerFactory.getLogger(TaskProducer.class);

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void sendEvent(String queueName, Event<? extends IMessage> event) {
		rabbitTemplate.convertAndSend(queueName, event);
	}

	public void sendEvent(IMessage message) {

		Event<IMessage> event = new Event<>(EventType.ASYNC_TASK, message);
		rabbitTemplate.convertAndSend(TaskConstants.TASK_QUEUE_NAME, event);

		logger.info("Push message to queue: [{}]", TaskConstants.TASK_QUEUE_NAME);
	}

}

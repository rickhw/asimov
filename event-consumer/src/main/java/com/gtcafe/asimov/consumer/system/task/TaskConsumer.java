package com.gtcafe.asimov.consumer.system.task;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.system.QueueNameConstants;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;


@Service
public class TaskConsumer { // implements MessageListener {

    @Autowired
    TaskEventHandler _taskHandler;

    @RabbitListener(queues = QueueNameConstants.TASK_QUEUE_NAME)
    public void receiveEvent(TaskDomainObject event) {

		_taskHandler.handle(event);
    }

}

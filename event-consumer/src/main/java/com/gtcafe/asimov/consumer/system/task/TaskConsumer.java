package com.gtcafe.asimov.consumer.system.task;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.system.QueueNameConstants;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.system.task.TaskState;
import com.gtcafe.asimov.core.utils.JsonUtils;

@Service
public class TaskConsumer { // implements MessageListener {

  @Autowired
  TaskEventHandler _taskHandler;

  @Autowired
  private JsonUtils _jsonUtils;

  @RabbitListener(queues = QueueNameConstants.TASK_QUEUE_NAME)
  public void receiveEvent(String message) {
    TaskDomainObject tdo = _jsonUtils.jsonStringToModel(message, TaskDomainObject.class);

    TaskState state = tdo.getMetadata().get_state();

    if (TaskState.PENDING.equals(state)) {
      _taskHandler.transit(tdo, TaskState.RUNNING);
    } else {
      // TODO
      System.out.println("Unexpected state: " + state);
    }
    
  }

}

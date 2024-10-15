package com.gtcafe.asimov.consumer.platform;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.platform.PlatformQueueName;


@Service
public class PlatformConsumer { // implements MessageListener {

  // @Autowired
  // TaskEventHandler _taskHandler;

  @RabbitListener(queues = PlatformQueueName.SAY_HELLO_QUEUE_NAME)
  public void receiveEvent(String message) {

    // _taskHandler.handle(event);
    System.out.println("message: " + message);

    // update status
  }

}

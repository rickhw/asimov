package com.gtcafe.asimov.consumer.platform;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.constants.QueueName;
import com.gtcafe.asimov.core.utils.JsonUtils;


@Service
public class PlatformConsumer { // implements MessageListener {

  @Autowired
  private JsonUtils _jsonUtils;

  @Autowired
  CacheRepository _cacheRepos;
  
  @RabbitListener(queues = QueueName.SAY_HELLO_QUEUE_NAME)
  public void receiveEvent(String message) {

    // I/O blocking
    try {
      // _taskHandler.handle(event);
      Thread.sleep(10000);
      System.out.println("message: " + message);

    } catch (Exception ex) {

    }

    // update status
  }

}

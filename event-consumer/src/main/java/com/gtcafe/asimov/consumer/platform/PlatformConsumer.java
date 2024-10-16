package com.gtcafe.asimov.consumer.platform;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.platform.PlatformQueueName;
import com.gtcafe.asimov.core.utils.JsonUtils;


@Service
public class PlatformConsumer { // implements MessageListener {

  @Autowired
  private JsonUtils _jsonUtils;

  @Autowired
  CacheRepository _cacheRepos;
  
  @RabbitListener(queues = PlatformQueueName.SAY_HELLO_QUEUE_NAME)
  public void receiveEvent(String message) {

    // _taskHandler.handle(event);
    System.out.println("message: " + message);

    // update status
  }

}

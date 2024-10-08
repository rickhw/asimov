package com.gtcafe.asimov.apiserver.system.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;


@Service
public class TaskService {

  @Autowired
  MessageProducer _producer;


  public void createTaskAsync() {

  }

  // TODO
  public TaskDomainObject retriveTask(String id) {

    return null;
  }

  // TODO
  public void prugeaskAsync(String id) {

  }


}

package com.gtcafe.asimov.apiserver.platform.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.task.pojo.TaskDomainObject;


@Service
public class TaskService {

  @Autowired
  TaskProducer _producer;


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

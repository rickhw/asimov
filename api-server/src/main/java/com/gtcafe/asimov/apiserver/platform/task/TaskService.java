package com.gtcafe.asimov.apiserver.platform.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

  @Autowired
  TaskProducer _producer;


  public void createTaskAsync() {

  }
}

package com.gtcafe.asimov.rest.domain.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.rest.domain.task.response.RetrieveTaskResponse;
import com.gtcafe.asimov.system.hello.consumer.HelloEvent;
import com.gtcafe.asimov.system.task.TaskMapper;
import com.gtcafe.asimov.system.task.domain.TaskService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Platform/Task", description = "Task API")
@RestController
@RequestMapping("/api/v1alpha/tasks")
@Slf4j
public class TaskController {

  @Autowired
  private TaskService _service;

  @Autowired
  private TaskMapper _mapper;


  @GetMapping(value = "/{task-id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<RetrieveTaskResponse> retrieve(@PathVariable(name = "task-id") String taskId) {
    log.info("taskId: [{}]", taskId);
    // 1. validate: is not exist or expire.

    // 2. find the id in cache
    HelloEvent event = _service.retrieve(taskId);

    // tdo.getData()

    // 3. Convert
    RetrieveTaskResponse res = _mapper.event2Response(event);

    return ResponseEntity.ok(res);
  }

  // @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  // public ResponseEntity<String> purgeAsync(@PathVariable String id) {

  //   Event<DeleteTenantMessage> event = _producer.sendDeleteEvent(id);
  //   IMessage message = (IMessage) event.getData();

  //   return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  // }

}

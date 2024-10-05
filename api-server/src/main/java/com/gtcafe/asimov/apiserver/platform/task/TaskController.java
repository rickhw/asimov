package com.gtcafe.asimov.apiserver.platform.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.apiserver.platform.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.apiserver.platform.task.pojo.TaskDomainObject;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  @Autowired
  TaskService _service;

  @Autowired
  TaskDataTransferObject _dto;

  // @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  // public ResponseEntity<String> rootPath() {
  //   return ResponseEntity.ok("ok");
  // }

  // @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  // public ResponseEntity<RetrieveTaskResponse> createTaskAsync(
  //       @RequestBody CreateTaskRequest request) {

  //   RetrieveTaskResponse response = new RetrieveTaskResponse();

  //   return ResponseEntity.ok(response);
  // }


  @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<RetrieveTaskResponse> retrieve(@PathVariable String id) {
    // 1. validate: is not exist or expire.

    // 2. find the id in cache
    TaskDomainObject tdo = _service.retriveTask(id);
    RetrieveTaskResponse res = _dto.convertToTaskResponse(tdo);

    // return ResponseEntity.ok(new RetrieveTaskResponse(id));
    return ResponseEntity.ok(res);
  }

  // @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  // public ResponseEntity<String> deleteAsync(@PathVariable String id) {

  //   Event<DeleteTenantMessage> event = _producer.sendDeleteEvent(id);
  //   IMessage message = (IMessage) event.getData();

  //   return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  // }

}

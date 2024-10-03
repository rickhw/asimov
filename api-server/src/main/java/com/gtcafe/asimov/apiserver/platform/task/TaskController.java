package com.gtcafe.asimov.apiserver.platform.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.IMessage;

import com.gtcafe.asimov.apiserver.platform.task.operation.*;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  @Autowired
  TaskService _service;

  @GetMapping(value = "", produces = { "application/json" })
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }

  @PostMapping(value = "", produces = { "application/json" })
  public ResponseEntity<RetrieveTaskResponse> createTaskAsync(
        @RequestBody CreateTaskRequest request) {


    RetrieveTaskResponse response = new RetrieveTaskResponse();

    return ResponseEntity.ok(request.toString());
  }


  @GetMapping(value = "/{id}", produces = { "application/json" })
  public ResponseEntity<String> retrieve(@PathVariable String id) {
    // 1. validate: is not exist or expire.

    // 2. find

    // return ResponseEntity.ok(new RetrieveTaskResponse(id));
    return ResponseEntity.ok("ok");
  }

  // @DeleteMapping(value = "/{id}", produces = { "application/json" })
  // public ResponseEntity<String> deleteAsync(@PathVariable String id) {

  //   Event<DeleteTenantMessage> event = _producer.sendDeleteEvent(id);
  //   IMessage message = (IMessage) event.getData();

  //   return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  // }

}

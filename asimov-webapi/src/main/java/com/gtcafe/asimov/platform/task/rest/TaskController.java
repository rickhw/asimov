package com.gtcafe.asimov.platform.task.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.platform.task.domain.TaskService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Task", description = "Task API")
@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {
  // private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

  @Autowired
  TaskService _service;

  @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> retrieve(@PathVariable String id) {
    // 1. validate: is not exist or expire.

    // 2. find the id in cache
    // HelloEvent tdo = _service.retrieveV4(id);

    // tdo.getData()

    // 3. DTO
    // RetrieveTaskResponse res = _dto.convertToTaskV4Response(tdo);

    return ResponseEntity.ok("ok");
  }

  // @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  // public ResponseEntity<String> purgeAsync(@PathVariable String id) {

  //   Event<DeleteTenantMessage> event = _producer.sendDeleteEvent(id);
  //   IMessage message = (IMessage) event.getData();

  //   return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  // }

}

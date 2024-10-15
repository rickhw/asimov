package com.gtcafe.asimov.apiserver.system.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.apiserver.system.CacheRepository;
import com.gtcafe.asimov.apiserver.system.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;
import com.gtcafe.asimov.core.utils.JsonUtils;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

  @Autowired
  CacheRepository _repos;

  @Autowired
  TaskDataTransferObject _dto;

  @Autowired
  private JsonUtils jsonUtils;


  // private final ObjectMapper objectMapper;
  
  // public TaskController() {
  //   this.objectMapper = new ObjectMapper();
  //   this.objectMapper.registerModule(new JavaTimeModule());
  // }


  @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<RetrieveTaskResponse> retrieve(@PathVariable String id) {
    // 1. validate: is not exist or expire.

    // 2. find the id in cache
    String jsonString = _repos.retrieveObject(id);
    logger.info("TaskObject: {}", jsonString);
    // TaskDomainObject tdo = objectMapper.convertValue(jsonString, TaskDomainObject.class);
    TaskDomainObject tdo = jsonUtils.jsonStringToModel(jsonString, TaskDomainObject.class);

    // 3. DTO
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

package com.gtcafe.asimov.apiserver.rest.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import com.gtcafe.asimov.apiserver.producer.ContainerProducer;
import com.gtcafe.asimov.apiserver.rest.model.request.CreateContainerRequest;

import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.IMessage;
import com.gtcafe.asimov.core.event.message.container.CreateContainerMessage;
import com.gtcafe.asimov.core.event.message.container.DeleteContainerMessage;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/container")
public class ContainerController {

  @Autowired
  ContainerProducer _producer;

  @GetMapping(value = "", produces = { "application/json" })
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }


  @PostMapping(value = "", produces = { "application/json" })
  public ResponseEntity<String> createContainerAsync(
      @RequestBody
			@Validated
			CreateContainerRequest request) {

      // CreateContainerMessage message

    // Event<CreateContainerMessage> event = _producer.sendCreateContainerEvent(message);

    return ResponseEntity.ok("ok");
    // return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]", event.getEventId(), message));
  }

  @DeleteMapping(value = "/{id}", produces = { "application/json" })
  public ResponseEntity<String> deleteContainerAsync(@PathVariable String id) {

    Event<DeleteContainerMessage> event = _producer.sendDeleteContainerMessageEvent(id);
    IMessage message = (IMessage) event.getData();

    return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  }

}

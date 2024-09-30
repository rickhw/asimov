package com.gtcafe.asimov.apiserver.controller.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gtcafe.asimov.apiserver.services.MessageProducer;
//import com.gtcafe.asimov.apiserver.model.Event;
//import com.gtcafe.asimov.apiserver.model.Message;

import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.IMessage;
import com.gtcafe.asimov.core.event.message.CreateContainerMessage;
import com.gtcafe.asimov.core.event.message.DeleteContainerMessage;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api")
public class RootController {

  @Autowired
  MessageProducer _producer;

  @GetMapping(value = "/", produces = { "application/json" })
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }

//  @PostMapping(value = "/send-message", produces = { "application/json" })
//  public ResponseEntity<String> sendMessage(
//      @RequestBody Message message) {
//
//    String eventId = UUID.randomUUID().toString();
//    message.setId(eventId);
//
//    Event event = new Event();
//    event.setEventId(eventId);
//    event.setMessage(message);
//
//    _producer.send(event);
//
//    return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]", eventId, message));
//  }

  @PostMapping(value = "/container", produces = { "application/json" })
  public ResponseEntity<String> createContainerAsync(
          @RequestBody CreateContainerMessage message) {

    Event<CreateContainerMessage> event = _producer.sendCreateContainerEvent(message);

    return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]", event.getEventId(), message));
  }

  @DeleteMapping(value = "/container/{id}", produces = { "application/json" })
  public ResponseEntity<String> deleteContainerApiAsync(@PathVariable String id) {

    Event<DeleteContainerMessage> event = _producer.sendDeleteContainerMessageEvent(id);
    IMessage message = (IMessage) event.getData();

    return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  }

}

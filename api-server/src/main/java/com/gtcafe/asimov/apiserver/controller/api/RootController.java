package com.gtcafe.asimov.apiserver.controller.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.apiserver.services.MessageProducer;
import com.gtcafe.asimov.apiserver.model.Event;
import com.gtcafe.asimov.apiserver.model.Message;

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

  @PostMapping(value = "/send-message", produces = { "application/json" })
  public ResponseEntity<String> sendMessage(
      @RequestBody Message message) {

    String eventId = UUID.randomUUID().toString();
    message.setId(eventId);

    Event event = new Event();
    event.setEventId(eventId);
    event.setMessage(message);

    _producer.send(event);

    return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]", eventId, message));
  }

}

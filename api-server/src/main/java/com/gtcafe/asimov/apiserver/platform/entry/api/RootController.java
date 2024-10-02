package com.gtcafe.asimov.apiserver.platform.entry.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api")
public class RootController {

  // @Autowired
  // MessageProducer _producer;

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


}

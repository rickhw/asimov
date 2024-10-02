package com.gtcafe.asimov.apiserver.platform.tenant;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.IMessage;

import com.gtcafe.asimov.core.platform.tenant.*;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/tenant")
public class TenantController {

  @Autowired
  TenantProducer _producer;

  @GetMapping(value = "", produces = { "application/json" })
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }


  @PostMapping(value = "", produces = { "application/json" })
  public ResponseEntity<String> createAsync(
      @RequestBody
			@Validated
			CreateTenantRequest request) {

      // CreateContainerMessage message

    // Event<CreateTenantMessage> event = _producer.sendCreateContainerEvent(message);

    return ResponseEntity.ok("ok");
    // return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]", event.getEventId(), message));
  }

  @GetMapping(value = "/{id}", produces = { "application/json" })
  public ResponseEntity<String> retrieve(@PathVariable String id) {
    return ResponseEntity.ok("ok");
  }

  @DeleteMapping(value = "/{id}", produces = { "application/json" })
  public ResponseEntity<String> deleteAsync(@PathVariable String id) {

    Event<DeleteTenantMessage> event = _producer.sendDeleteEvent(id);
    IMessage message = (IMessage) event.getData();

    return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  }

}

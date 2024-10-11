package com.gtcafe.asimov.apiserver.domain.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.core.domain.container.DeleteContainerMessage;
import com.gtcafe.asimov.core.domain.event.Event;
import com.gtcafe.asimov.core.domain.event.IMessage;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/container")
public class ContainerController {

  @Autowired
  ContainerProducer _producer;

  @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }


  @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<String> createContainerAsync(
      @RequestBody
			@Validated
			CreateContainerRequest request) {

      // CreateContainerMessage message

    // Event<CreateContainerMessage> event = _producer.sendCreateContainerEvent(message);

    return ResponseEntity.ok("ok");
    // return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]", event.getEventId(), message));
  }

  @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<String> deleteContainerAsync(@PathVariable String id) {

    Event<DeleteContainerMessage> event = _producer.sendDeleteContainerMessageEvent(id);
    IMessage message = (IMessage) event.getData();

    return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  }

}

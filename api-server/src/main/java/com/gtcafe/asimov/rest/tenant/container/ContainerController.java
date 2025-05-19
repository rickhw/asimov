package com.gtcafe.asimov.rest.tenant.container;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.rest.tenant.container.request.CreateContainerRequest;

import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/v1alpha/container")
@Tag(name = "Domain/Container", description = "Container APIs")
public class ContainerController {

  // @Autowired
  // ContainerProducer _producer;

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

  // @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  // public ResponseEntity<String> deleteContainerAsync(@PathVariable String id) {

  //   Event<DeleteContainerMessage> event = _producer.sendDeleteContainerMessageEvent(id);
  //   IMessage message = (IMessage) event.getData();

  //   return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
  // }

}

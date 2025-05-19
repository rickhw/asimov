package com.gtcafe.asimov.rest.tenant.volume;

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

import com.gtcafe.asimov.rest.tenant.volume.request.CreateVolumeRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/v1alpha/volumes")
@Tag(name = "Domain/Volumes", description = "Volume APIs")
public class VolumeController {

  // @Autowired
  // ContainerProducer _producer;

  @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  @Schema(name = "Query the volumes", description = "")
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }


  @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  @Schema(name = "Create a volume", description = "")
  public ResponseEntity<String> createVolumeAsync(
      @RequestBody
			@Validated
			CreateVolumeRequest request) {
        
    return ResponseEntity.ok("ok");
    // return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]", event.getEventId(), message));
  }

  @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  @Schema(name = "Delete a volume", description = "")
  public ResponseEntity<String> deleteVolumeAsync(@PathVariable String id) {

    // Event<DeleteContainerMessage> event = _producer.sendDeleteContainerMessageEvent(id);
    // IMessage message = (IMessage) event.getData();

    return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]"));
  }

}

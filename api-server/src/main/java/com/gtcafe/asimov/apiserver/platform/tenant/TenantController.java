package com.gtcafe.asimov.apiserver.platform.tenant;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.IMessage;

import com.gtcafe.asimov.apiserver.platform.tenant.operation.*;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

  @Autowired
  TenantService _service;

  @GetMapping(value = "", produces = { "application/json" })
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }

  @PostMapping(value = "", produces = { "application/json" })
  public ResponseEntity<String> registerAsync(
      @RequestBody
			@Validated
			CreateTenantRequest request) {

    // 1. validate the request --> by spring-boot-starter-validation

    // 2. validate the profile, example: serviceQuota. check the TenantProfile in memory

    // 3. validate associate.

    // 4. gen the id

    // 5. create async task
    _service.registerTenantAsync(request);

    // 6. return the task status.
    return ResponseEntity.ok("ok");
  }

  @GetMapping(value = "/{id}", produces = { "application/json" })
  public ResponseEntity<String> retrieve(@PathVariable String id) {
    return ResponseEntity.ok("ok");
  }

  @DeleteMapping(value = "/{id}", produces = { "application/json" })
  public ResponseEntity<String> deregisterAsync(@PathVariable String id) {

    // 1. validate id

    // 2. create async task
    _service.deregisterAsync(id);

    // 3. return the task status.
    return ResponseEntity.ok("ok");
  }

}

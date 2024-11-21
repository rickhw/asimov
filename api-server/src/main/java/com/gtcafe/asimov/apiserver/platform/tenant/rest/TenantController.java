package com.gtcafe.asimov.apiserver.platform.tenant.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.apiserver.platform.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.apiserver.platform.tenant.domain.TenantService;
import com.gtcafe.asimov.apiserver.platform.tenant.dto.RegisterTenantDTO;
import com.gtcafe.asimov.core.platform.tenant.RegisterTenantEvent;

// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

  @Autowired
  TenantService _service;

  @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }

  @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<RetrieveTaskResponse> registerAsync(
      @RequestBody @Validated RegisterTenantDTO request) {

    // 1. validate the request --> by spring-boot-starter-validation

    // 2. validate the profile, example: serviceQuota. check the TenantProfile in
    // memory

    // 3. validate associate.

    // 4. gen the id

    // 5. create async task
    RegisterTenantEvent event = _service.registerTenantAsync(request);
    RetrieveTaskResponse res = new RetrieveTaskResponse(event);

    // 6. return the task status.
    return ResponseEntity.ok(res);
  }

  @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<String> retrieve(@PathVariable String id) {
    return ResponseEntity.ok("ok");
  }

  // @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  // public ResponseEntity<String> deregisterAsync(@PathVariable String id) {

  //   // 1. validate id

  //   // 2. create async task
  //   _service.deregisterAsync(id);

  //   // 3. return the task status.
  //   return ResponseEntity.ok("ok");
  // }

}

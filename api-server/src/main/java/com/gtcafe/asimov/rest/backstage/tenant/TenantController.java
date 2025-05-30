package com.gtcafe.asimov.rest.backstage.tenant;

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

import com.gtcafe.asimov.rest.backstage.tenant.request.RegisterTenantRequest;
import com.gtcafe.asimov.rest.backstage.tenant.response.TenantTaskResponse;
import com.gtcafe.asimov.system.tenant.domain.TenantService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Platform/Tenant", description = "Tenant API")
@RestController
@RequestMapping("/api/v1alpha/tenants")
public class TenantController {

  @Autowired
  TenantService _service;

  @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<String> rootPath() {
    return ResponseEntity.ok("ok");
  }

  @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<TenantTaskResponse> registerAsync(
      @RequestBody @Validated RegisterTenantRequest request) {

    // 1. validate the request --> by spring-boot-starter-validation

    // 2. validate the profile, example: serviceQuota. check the TenantProfile in
    // memory

    // 3. validate associate.

    // 4. gen the id

    // 5. create async task
    TenantTaskResponse res = _service.registerTenantAsync(request);

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

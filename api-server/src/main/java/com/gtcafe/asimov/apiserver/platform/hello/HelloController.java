package com.gtcafe.asimov.apiserver.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gtcafe.asimov.apiserver.platform.hello.operation.HelloRequest;
import com.gtcafe.asimov.apiserver.platform.hello.operation.HelloResponse;
import com.gtcafe.asimov.apiserver.platform.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.core.constants.HttpHeaderConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class HelloController {

  @Autowired
  private HelloService _service;

  @GetMapping(value = "/hello", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<HelloResponse> helloSync() {

    HelloResponse res = _service.handler("Hello World");
    return ResponseEntity.ok(res);
  }

  @PostMapping(value = "/hello", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> hello(
      @Valid @RequestBody HelloRequest request,
      @RequestHeader(value = HttpHeaderConstants.X_REQUEST_MODE, required = false) String requestMode) {

    if (HttpHeaderConstants.ASYNC_MODE.equalsIgnoreCase(requestMode)) {
      RetrieveTaskResponse res = _service.handlerAsync(request.getMessage());
      return ResponseEntity.ok(res);
    } else {
      HelloResponse res = _service.handler(request);

      return ResponseEntity.ok(res);
    }
  }

  // @GetMapping(value = "/", produces = { "application/json" })
  // public ResponseEntity<String> helloAsync() {

  // return ResponseEntity.ok("ok");
  // }

}

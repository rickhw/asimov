package com.gtcafe.asimov.apiserver.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.apiserver.platform.hello.dto.HelloDto;
import com.gtcafe.asimov.apiserver.platform.hello.response.HelloResponse;
import com.gtcafe.asimov.apiserver.platform.task.response.RetrieveTaskResponse;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.system.constants.HttpHeaderConstants;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloController {

  @Autowired
  private HelloService _service;

  @GetMapping(value = "/hello", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<HelloResponse> helloSync() {
    String message = _service.sayHelloSync("Hello World");
    HelloResponse res = new HelloResponse(message);
    return ResponseEntity.ok(res);
  }

  @PostMapping(value = "/hello", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> hello(
      @Valid @RequestBody HelloDto request,
      @RequestHeader(value = HttpHeaderConstants.X_REQUEST_MODE, required = false) String requestMode
  ) {

    if (HttpHeaderConstants.ASYNC_MODE.equalsIgnoreCase(requestMode)) {

      SayHelloEvent event = _service.sayHelloAsync(request.getMessage());
      RetrieveTaskResponse res = new RetrieveTaskResponse(event);

      return ResponseEntity.ok(res);
    } else {

      String message = _service.sayHelloSync(request.getMessage());
      HelloResponse res = new HelloResponse(message);

      return ResponseEntity.ok(res);
    }
  }
}

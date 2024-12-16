package com.gtcafe.asimov.platform.hello.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.system.constants.HttpHeaderConstants;
import com.gtcafe.asimov.platform.hello.domain.Hello;
import com.gtcafe.asimov.platform.hello.domain.HelloService;
import com.gtcafe.asimov.platform.hello.rest.request.SayHelloRequest;
import com.gtcafe.asimov.platform.hello.rest.response.SayHelloResponse;
import com.gtcafe.asimov.platform.task.rest.response.RetrieveTaskResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloController {

  @Autowired
  private HelloService _service;

  @GetMapping(value = "/hello", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<SayHelloResponse> sayHelloSync() {
    Hello message = _service.sayHelloSync();
    SayHelloResponse res = new SayHelloResponse(message);
    return ResponseEntity.ok(res);
  }

  @PostMapping(value = "/hello", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> sayHello(
      @Valid @RequestBody SayHelloRequest request,
      @RequestHeader(value = HttpHeaderConstants.X_REQUEST_MODE, required = false) String requestMode
  ) {

    if (HttpHeaderConstants.ASYNC_MODE.equalsIgnoreCase(requestMode)) {

      SayHelloEvent event = _service.sayHelloAsync(request.getHello());
      RetrieveTaskResponse res = new RetrieveTaskResponse(event);

      return ResponseEntity.ok(res);
    } else {

      Hello message = _service.sayHelloSync();
      SayHelloResponse res = new SayHelloResponse(message);

      return ResponseEntity.ok(res);
    }
  }
}

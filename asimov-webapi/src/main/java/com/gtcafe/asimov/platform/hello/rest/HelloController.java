package com.gtcafe.asimov.platform.hello.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.core.platform.hello.Hello;
import com.gtcafe.asimov.core.system.constants.HttpHeaderConstants;
import com.gtcafe.asimov.core.system.utils.TimeUtils;
import com.gtcafe.asimov.platform.hello.HelloMapper;
import com.gtcafe.asimov.platform.hello.domain.HelloService;
import com.gtcafe.asimov.platform.hello.rest.request.SayHelloRequest;
import com.gtcafe.asimov.platform.hello.rest.response.HelloTaskResponse;
import com.gtcafe.asimov.platform.hello.rest.response.SayHelloResponse;
import com.gtcafe.asimov.platform.task.domain.schema.ExecMode;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1alpha/hello")
@Tag(name = "Platform/Hello", description = "Hello API")
@Slf4j
public class HelloController {

  @Autowired
  private HelloService _service;

  @Autowired
  private HelloMapper _mapper;

  @Autowired
  private TimeUtils timeUtils;


  @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<SayHelloResponse> sayHelloSync() {
    Hello hello = _service.sayHelloSync();

    SayHelloResponse res =  SayHelloResponse.builder()
      .message(hello)
      .launchTime(timeUtils.currentTimeIso8601())
      .build();


    return ResponseEntity.ok(res);
  }

  @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  @Parameter(name = HttpHeaderConstants.X_REQUEST_MODE, description = "Request mode", example = "async", schema = @Schema(implementation = ExecMode.class))
  public ResponseEntity<HelloTaskResponse> sayHello(
      @Valid @RequestBody SayHelloRequest request
  ) {

    // if (HttpHeaderConstants.ASYNC_MODE.equalsIgnoreCase(requestMode)) {
    Hello hello = _mapper.mapRequestToDomain(request);
    HelloTaskResponse res = _service.sayHelloAsync(hello);
    //   RetrieveTaskResponse res = new RetrieveTaskResponse(event);

    return ResponseEntity.ok(res);
    // } else {

    //   Hello message = _service.sayHelloSync();
    //   SayHelloResponse res = new SayHelloResponse(message);

    //   return ResponseEntity.ok(res);
    // }
  }
}

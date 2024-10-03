package com.gtcafe.asimov.apiserver.platform.hello;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gtcafe.asimov.apiserver.utils.Slogan;


// @Tag(name = "API Metadata", description = "")
@RestController
@RequestMapping("/api/hello")
public class HelloController {

  // @Autowired
  // MessageProducer _producer;

  @Autowired
  private Slogan utils;


  @GetMapping(value = "/", produces = { "application/json" })
  public ResponseEntity<HelloResponse> helloSync(@RequestBody HelloRequest request) {

    HelloResponse res = new HelloResponse(request.getMessage());

    return ResponseEntity.ok(res);
  }

  // @GetMapping(value = "/", produces = { "application/json" })
  // public ResponseEntity<String> helloAsync() {
    
  //   return ResponseEntity.ok("ok");
  // }

}

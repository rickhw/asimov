package com.gtcafe.asimov.apiserver.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.utils.Slogan;

@Service
public class HelloService {

  @Autowired
  private Slogan utils;

  // 專注處理 Biz Logic,
  // 1. 不處理組 DTO 的任務
  // 2. 應該沒有其他的 import, 只有 POJO

  // TODO: 把 HTTP Messsage (Request/Response) 的東西搬出去
  public HelloResponse handler(HelloRequest request) {
    HelloResponse res = new HelloResponse(request.getMessage());

    // 處理核心商業邏輯

    return res;
  }


  // TODO: 把 HTTP Messsage (Request/Response) 的東西搬出去
  public HelloResponse handler(String message) {
    HelloResponse res = new HelloResponse(message);

    // 處理核心商業邏輯

    return res;
  }

}

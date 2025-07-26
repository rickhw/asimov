package com.gtcafe.asimov.rest.domain.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.framework.constants.HttpHeaderConstants;
import com.gtcafe.asimov.framework.utils.TimeUtils;
import com.gtcafe.asimov.rest.domain.hello.request.SayHelloRequest;
import com.gtcafe.asimov.rest.domain.hello.response.HelloTaskResponse;
import com.gtcafe.asimov.rest.domain.hello.response.SayHelloResponse;
import com.gtcafe.asimov.system.hello.HelloMapper;
import com.gtcafe.asimov.system.hello.domain.HelloService;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
import com.gtcafe.asimov.system.task.schema.ExecMode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1alpha/hello")
@Tag(
  name = "Platform/Hello", 
  description = "Hello API - 提供同步和非同步的問候訊息處理功能，支援輸入驗證、快取和監控"
)
@Slf4j
public class HelloController {

  @Autowired
  private HelloService _service;

  @Autowired
  private HelloMapper _mapper;

  @Autowired
  private TimeUtils timeUtils;


  @Operation(
    summary = "同步問候",
    description = "立即返回一個預設的問候訊息，適用於簡單的健康檢查或測試用途"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "成功返回問候訊息",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = SayHelloResponse.class),
        examples = @ExampleObject(
          name = "成功回應",
          value = """
            {
              "message": {
                "message": "Hello, World!"
              },
              "launchTime": "2024-01-20T10:30:00.000Z"
            }
            """
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "內部伺服器錯誤",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        examples = @ExampleObject(
          name = "錯誤回應",
          value = """
            {
              "error": "Internal Server Error",
              "message": "Failed to process hello request"
            }
            """
        )
      )
    )
  })
  @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<SayHelloResponse> sayHelloSync() {

    log.info("sayHelloSync()");

    Hello hello = _service.sayHelloSync();

    SayHelloResponse res =  SayHelloResponse.builder()
      .message(hello)
      .launchTime(timeUtils.currentTimeIso8601())
      .build();


    return ResponseEntity.ok(res);
  }

  @Operation(
    summary = "非同步問候",
    description = """
      接收自訂問候訊息並進行非同步處理。
      
      功能特色：
      - 輸入驗證：檢查訊息長度、格式和安全性
      - 非同步處理：立即返回任務 ID，後台處理
      - 快取儲存：將結果儲存到 Redis 快取
      - 資料庫持久化：將訊息儲存到資料庫
      - 佇列處理：發送到 RabbitMQ 進行後續處理
      - 監控指標：記錄處理時間和成功率
      
      安全防護：
      - 防範 XSS 攻擊
      - 防範 SQL 注入
      - 檢查禁用關鍵字
      - 限制訊息長度
      """
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "成功接收請求並開始處理",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = HelloTaskResponse.class),
        examples = @ExampleObject(
          name = "成功回應",
          value = """
            {
              "id": "123e4567-e89b-12d3-a456-426614174000",
              "creationTime": 1705747800000,
              "state": "PENDING",
              "data": {
                "message": "Hello, Asimov!"
              },
              "finishedAt": null
            }
            """
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "請求驗證失敗",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        examples = @ExampleObject(
          name = "驗證錯誤",
          value = """
            {
              "error": "Validation Failed",
              "message": "Message length must be at least 1 characters",
              "details": [
                "Message cannot be empty or contain only whitespace"
              ]
            }
            """
        )
      )
    ),
    @ApiResponse(
      responseCode = "422",
      description = "安全威脅檢測",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        examples = @ExampleObject(
          name = "安全錯誤",
          value = """
            {
              "error": "Security Threat Detected",
              "message": "Message cannot contain HTML tags",
              "threatType": "xss_attempt"
            }
            """
        )
      )
    )
  })
  @PostMapping(value = "", 
    consumes = { MediaType.APPLICATION_JSON_VALUE }, 
    produces = { MediaType.APPLICATION_JSON_VALUE }
  )
  @Parameter(
    name = HttpHeaderConstants.X_REQUEST_MODE, 
    description = "請求模式 (目前僅支援 async)", 
    example = "async", 
    schema = @Schema(implementation = ExecMode.class)
  )
  public ResponseEntity<HelloTaskResponse> sayHelloAsync(
      @Parameter(
        description = "問候請求內容",
        required = true,
        content = @Content(
          examples = {
            @ExampleObject(
              name = "基本問候",
              value = """
                {
                  "message": "Hello, World!"
                }
                """,
              description = "簡單的問候訊息"
            ),
            @ExampleObject(
              name = "個人化問候",
              value = """
                {
                  "message": "Hello, Asimov! How are you today?"
                }
                """,
              description = "個人化的問候訊息"
            ),
            @ExampleObject(
              name = "多語言問候",
              value = """
                {
                  "message": "你好，世界！"
                }
                """,
              description = "中文問候訊息"
            )
          }
        )
      )
      @Valid @RequestBody SayHelloRequest request,
      @RequestHeader(name = HttpHeaderConstants.X_REQUEST_MODE, required = false, defaultValue = HttpHeaderConstants.V__ASYNC_MODE) String requestMode
  ) {

    log.info("requestMode: [{}]", requestMode);

    Hello hello = _mapper.mapRequestToDomain(request);
    HelloEvent event = _service.sayHelloAsync(hello);

    HelloTaskResponse res = _mapper.mapHelloEventToResponse(event);

    return ResponseEntity.ok(res);
  }
}

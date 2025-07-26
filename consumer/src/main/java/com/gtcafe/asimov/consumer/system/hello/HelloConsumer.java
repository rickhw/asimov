package com.gtcafe.asimov.consumer.system.hello;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.gtcafe.asimov.consumer.system.hello.service.HelloConsumerMetricsService;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfigService;
import com.gtcafe.asimov.system.hello.model.HelloEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HelloConsumer {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private HelloEventHandler helloEventHandler;

    @Autowired
    private HelloQueueConfigService queueConfigService;
    
    @Autowired
    private HelloConsumerMetricsService metricsService;

    @RabbitListener(queues = "#{helloQueueConfigService.queueName}")
    public void receiveMessage(@Payload String eventString) {
        log.info("HelloConsumer.receiveMessage() eventString: {}", eventString);
        
        // 記錄訊息大小
        metricsService.recordMessageSize(eventString.getBytes().length);

        Optional<HelloEvent> eventOptional = jsonUtils.jsonStringToModelSafe(eventString, HelloEvent.class);

        if (eventOptional.isEmpty()) {
            log.error("Failed to deserialize message to HelloEvent: {}", eventString);
            metricsService.recordMessageFailed("json_parsing_error");
            // If deserialization fails, we treat it as a permanent failure and send to DLQ
            throw new RuntimeException("Deserialization failed for message: " + eventString);
        }

        HelloEvent event = eventOptional.get();

        try {
            boolean processed = helloEventHandler.handleEvent(event);
            if (!processed) {
                // If handleEvent returns false, it means the business logic failed
                // We re-throw to trigger the DLQ mechanism
                throw new RuntimeException("Business logic failed for event: " + event.getId());
            }
            log.info("Processed event successfully: {}", event.getId());
        } catch (Exception e) {
            log.error("Error processing event: {}", event.getId(), e);
            // Re-throw the exception to trigger the DLQ mechanism
            throw e;
        }
    }

    @RabbitListener(queues = "#{helloQueueConfigService.deadLetterQueueName}")
    public void receiveDeadLetterMessage(@Payload String eventString) {
        log.error("Received dead-lettered message on queue [{}]: {}. Storing for analysis.", 
            queueConfigService.getDeadLetterQueueName(), eventString);
            
        // 記錄 DLQ 訊息
        metricsService.recordDlqMessage("processing_failed");
        
        // 嘗試解析事件以獲取更多資訊
        Optional<HelloEvent> eventOpt = jsonUtils.jsonStringToModelSafe(eventString, HelloEvent.class);
        if (eventOpt.isPresent()) {
            HelloEvent event = eventOpt.get();
            log.error("DLQ message details - Event ID: {}, Message: {}, State: {}", 
                event.getId(), 
                event.getData() != null ? event.getData().getMessage() : "null",
                event.getState());
                
            // 記錄狀態轉換到 DLQ
            String currentState = event.getState() != null ? event.getState().toString() : "UNKNOWN";
            metricsService.recordTaskStateTransition(currentState, "DLQ");
        }
        
        // Here you can add logic to handle the failed message,
        // e.g., save it to a database, send a notification, etc.
        handleDeadLetterMessage(eventString);
    }
    
    /**
     * 處理 DLQ 訊息的邏輯
     */
    private void handleDeadLetterMessage(String eventString) {
        // 實作 DLQ 訊息處理邏輯
        // 例如：儲存到資料庫、發送通知、記錄到檔案等
        log.info("Processing dead letter message: {}", eventString);
        // 可以在這裡實作：
        // 1. 儲存到專門的錯誤資料表
        // 2. 發送告警通知
        // 3. 記錄到錯誤日誌檔案
        // 4. 觸發人工處理流程
    }
}

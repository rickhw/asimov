package com.gtcafe.asimov.system.hello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Hello 佇列配置服務
 * 提供統一的佇列配置存取介面
 */
@Service
@Slf4j
public class HelloQueueConfigService {

    @Autowired
    private HelloQueueConfig helloQueueConfig;

    // DLQ 相關常數
    private static final String DLX_SUFFIX = ".dlx";
    private static final String DLQ_SUFFIX = ".dlq";

    /**
     * 獲取主要佇列名稱
     */
    public String getQueueName() {
        return helloQueueConfig.getQueueName();
    }

    /**
     * 獲取 Exchange 名稱
     */
    public String getExchangeName() {
        return helloQueueConfig.getExchangeName();
    }

    /**
     * 獲取 Routing Key 名稱
     */
    public String getRoutingKeyName() {
        return helloQueueConfig.getRoutingKeyName();
    }

    /**
     * 獲取 Dead Letter Exchange 名稱
     */
    public String getDeadLetterExchangeName() {
        return helloQueueConfig.getExchangeName() + DLX_SUFFIX;
    }

    /**
     * 獲取 Dead Letter Queue 名稱
     */
    public String getDeadLetterQueueName() {
        return helloQueueConfig.getQueueName() + DLQ_SUFFIX;
    }

    /**
     * 驗證配置是否完整
     */
    public boolean validateConfig() {
        boolean isValid = true;
        
        if (helloQueueConfig.getQueueName() == null || helloQueueConfig.getQueueName().trim().isEmpty()) {
            log.error("Queue name is not configured");
            isValid = false;
        }
        
        if (helloQueueConfig.getExchangeName() == null || helloQueueConfig.getExchangeName().trim().isEmpty()) {
            log.error("Exchange name is not configured");
            isValid = false;
        }
        
        if (helloQueueConfig.getRoutingKeyName() == null || helloQueueConfig.getRoutingKeyName().trim().isEmpty()) {
            log.error("Routing key name is not configured");
            isValid = false;
        }
        
        if (isValid) {
            log.info("Hello queue configuration validation passed");
            log.info("Queue: {}, Exchange: {}, Routing Key: {}", 
                getQueueName(), getExchangeName(), getRoutingKeyName());
        }
        
        return isValid;
    }

    /**
     * 獲取配置摘要
     */
    public String getConfigSummary() {
        return String.format("Queue: %s, Exchange: %s, Routing Key: %s, DLQ: %s", 
            getQueueName(), getExchangeName(), getRoutingKeyName(), getDeadLetterQueueName());
    }
}
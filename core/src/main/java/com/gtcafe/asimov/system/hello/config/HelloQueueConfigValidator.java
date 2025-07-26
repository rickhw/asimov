package com.gtcafe.asimov.system.hello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gtcafe.asimov.infrastructure.queue.RabbitInitializer;
import com.gtcafe.asimov.infrastructure.queue.model.QueueConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Hello 佇列配置驗證器
 * 在應用程式啟動時驗證 Producer 和 Consumer 配置的一致性
 */
@Component
@Order(100) // 確保在其他初始化之後執行
@Slf4j
public class HelloQueueConfigValidator implements ApplicationRunner {

    @Autowired
    private HelloQueueConfigService queueConfigService;

    @Autowired
    private RabbitInitializer rabbitInitializer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting Hello queue configuration validation...");
        
        // 驗證基本配置
        if (!queueConfigService.validateConfig()) {
            throw new IllegalStateException("Hello queue configuration validation failed");
        }
        
        // 驗證與 RabbitInitializer 的一致性
        validateConsistencyWithRabbitInitializer();
        
        log.info("Hello queue configuration validation completed successfully");
        log.info("Configuration summary: {}", queueConfigService.getConfigSummary());
    }

    /**
     * 驗證與 RabbitInitializer 配置的一致性
     */
    private void validateConsistencyWithRabbitInitializer() {
        if (rabbitInitializer.getQueues() == null || rabbitInitializer.getQueues().isEmpty()) {
            log.warn("RabbitInitializer queues configuration is empty, skipping consistency check");
            return;
        }

        String expectedQueueName = queueConfigService.getQueueName();
        String expectedExchangeName = queueConfigService.getExchangeName();
        String expectedRoutingKey = queueConfigService.getRoutingKeyName();

        // 檢查主要佇列配置
        QueueConfig mainQueueConfig = findQueueConfig(expectedQueueName);
        if (mainQueueConfig != null) {
            validateQueueConfig(mainQueueConfig, expectedQueueName, expectedExchangeName, expectedRoutingKey, "main queue");
        } else {
            log.warn("Main queue [{}] not found in RabbitInitializer configuration", expectedQueueName);
        }

        // 檢查 DLQ 配置
        String expectedDlqName = queueConfigService.getDeadLetterQueueName();
        String expectedDlxName = queueConfigService.getDeadLetterExchangeName();
        
        QueueConfig dlqConfig = findQueueConfig(expectedDlqName);
        if (dlqConfig != null) {
            validateQueueConfig(dlqConfig, expectedDlqName, expectedDlxName, expectedRoutingKey, "dead letter queue");
        } else {
            log.warn("Dead letter queue [{}] not found in RabbitInitializer configuration", expectedDlqName);
        }
    }

    /**
     * 尋找指定名稱的佇列配置
     */
    private QueueConfig findQueueConfig(String queueName) {
        return rabbitInitializer.getQueues().stream()
                .filter(config -> queueName.equals(config.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 驗證佇列配置的一致性
     */
    private void validateQueueConfig(QueueConfig config, String expectedQueue, String expectedExchange, 
                                   String expectedRoutingKey, String queueType) {
        boolean isValid = true;
        
        if (!expectedQueue.equals(config.getName())) {
            log.error("Queue name mismatch for {}: expected [{}], found [{}]", 
                queueType, expectedQueue, config.getName());
            isValid = false;
        }
        
        if (!expectedExchange.equals(config.getExchange())) {
            log.error("Exchange name mismatch for {}: expected [{}], found [{}]", 
                queueType, expectedExchange, config.getExchange());
            isValid = false;
        }
        
        if (!expectedRoutingKey.equals(config.getRoutingKey())) {
            log.error("Routing key mismatch for {}: expected [{}], found [{}]", 
                queueType, expectedRoutingKey, config.getRoutingKey());
            isValid = false;
        }
        
        if (isValid) {
            log.info("Configuration consistency validated for {}: queue=[{}], exchange=[{}], routingKey=[{}]", 
                queueType, config.getName(), config.getExchange(), config.getRoutingKey());
        } else {
            throw new IllegalStateException(String.format("Configuration mismatch detected for %s", queueType));
        }
    }
}
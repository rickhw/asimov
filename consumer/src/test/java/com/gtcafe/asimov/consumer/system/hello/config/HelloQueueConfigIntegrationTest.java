package com.gtcafe.asimov.consumer.system.hello.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.gtcafe.asimov.system.hello.config.HelloQueueConfigService;

/**
 * Hello 佇列配置整合測試
 * 驗證 Producer 和 Consumer 配置的一致性
 */
@SpringBootTest
@TestPropertySource(properties = {
    "asimov.system.hello.queues.task-queue.queue-name=hello.async.q",
    "asimov.system.hello.queues.task-queue.exchange-name=hello.async.ex",
    "asimov.system.hello.queues.task-queue.routing-key-name=hello.async.rk"
})
class HelloQueueConfigIntegrationTest {

    @Autowired
    private HelloQueueConfigService queueConfigService;

    @Test
    void testQueueConfigurationIsLoaded() {
        // 驗證配置服務已正確注入
        assertNotNull(queueConfigService);
        
        // 驗證配置值
        assertEquals("hello.async.q", queueConfigService.getQueueName());
        assertEquals("hello.async.ex", queueConfigService.getExchangeName());
        assertEquals("hello.async.rk", queueConfigService.getRoutingKeyName());
    }

    @Test
    void testDeadLetterQueueConfiguration() {
        // 驗證 DLQ 配置
        assertEquals("hello.async.ex.dlx", queueConfigService.getDeadLetterExchangeName());
        assertEquals("hello.async.q.dlq", queueConfigService.getDeadLetterQueueName());
    }

    @Test
    void testConfigurationValidation() {
        // 驗證配置驗證功能
        assertTrue(queueConfigService.validateConfig());
    }

    @Test
    void testConfigurationSummary() {
        // 驗證配置摘要
        String summary = queueConfigService.getConfigSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("hello.async.q"));
        assertTrue(summary.contains("hello.async.ex"));
        assertTrue(summary.contains("hello.async.rk"));
    }
}
package com.gtcafe.asimov.system.hello.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Hello 快取配置
 * 集中管理 Hello 相關的快取配置參數
 * 統一供 API-Server 和 Consumer 使用
 */
@Configuration
@ConfigurationProperties(prefix = "asimov.system.hello.cache")
@Data
public class HelloCacheConfig {

    /**
     * 主要快取的 TTL (預設 30 分鐘)
     */
    private Duration primaryTtl = Duration.ofMinutes(30);

    /**
     * 任務索引快取的 TTL (預設 24 小時)
     */
    private Duration taskIndexTtl = Duration.ofHours(24);

    /**
     * 分散式鎖的超時時間 (預設 10 秒)
     */
    private Duration lockTimeout = Duration.ofSeconds(10);

    /**
     * 快取鍵前綴
     */
    private String keyPrefix = "hello";

    /**
     * 是否啟用快取 (預設啟用)
     */
    private boolean enabled = true;

    /**
     * 快取失敗時是否拋出異常 (預設不拋出，只記錄警告)
     */
    private boolean failOnCacheError = false;

    /**
     * 快取統計是否啟用 (預設啟用)
     */
    private boolean metricsEnabled = true;

    /**
     * 快取預熱是否啟用 (預設不啟用)
     */
    private boolean warmupEnabled = false;

    /**
     * 快取預熱的批次大小 (預設 100)
     */
    private int warmupBatchSize = 100;
}
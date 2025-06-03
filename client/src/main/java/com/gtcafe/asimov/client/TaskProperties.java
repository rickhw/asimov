package com.gtcafe.asimov.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app.task")
@Data
public class TaskProperties {
    private String hostname;
    private int maxRequest;
    private int pollIntervalMs;
}

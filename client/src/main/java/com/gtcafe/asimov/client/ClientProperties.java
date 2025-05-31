package com.gtcafe.asimov.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "asimov.client")
@Data
public class ClientProperties {
    private String baseUrl;
    private String contentType;
    private String requestMode;
}

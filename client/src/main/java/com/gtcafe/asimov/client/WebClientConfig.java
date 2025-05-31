package com.gtcafe.asimov.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(ClientProperties props) {
        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader("Content-Type", props.getContentType())
                .defaultHeader("X-Request-Mode", props.getRequestMode())
                .build();
    }
}

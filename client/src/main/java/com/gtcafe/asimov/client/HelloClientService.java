package com.gtcafe.asimov.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HelloClientService {

    private final WebClient webClient;

    public HelloClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void postHelloMessage(String message) {
        webClient.post()
                .uri("/api/v1alpha/hello")
                .bodyValue("{\"message\": \"" + message + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .block(); // 對於模擬同步流程建議使用 block
    }

    public void getTaskStatus(String taskId) {
        webClient.get()
                .uri("/api/v1alpha/tasks/{id}", taskId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Task [" + taskId + "] status: " + response))
                .block();
    }
}

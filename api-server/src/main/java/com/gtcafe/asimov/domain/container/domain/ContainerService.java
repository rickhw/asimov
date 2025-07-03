package com.gtcafe.asimov.domain.container.domain;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContainerService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public void submitTask(String taskName) {
        // // log.info("index: [{}], message: [{}]", index, message);
        // // total.incrementAndGet();
        // try {
        //     HttpHeaders headers = new HttpHeaders();
        //     headers.setContentType(MediaType.APPLICATION_JSON);
        //     headers.set("X-Request-Mode", "async");

        //     Map<String, String> body = Map.of("message", "Hello Asimov, message is: " + taskName);
        //     HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        //     ResponseEntity<Map> response = restTemplate.postForEntity(
        //             props.getHostname() + "/api/v1alpha/hello", request, Map.class);

        //     log.warn("[#{}] 回傳狀態: {}, ContentType: {}", taskName, response.getStatusCode(), response.getHeaders().getContentType());
        //     log.warn("[#{}] 回傳內容: {}", taskName, response.getBody());
            
        //     String taskId = (String) response.getBody().get("id");
        //     if (taskId == null) {
        //         log.error("[#{}] ❌ Failed to get Task ID", taskName);
        //         // failure.incrementAndGet();
        //         // return CompletableFuture.completedFuture(null);
        //     }

        //     log.info("[#{}] ✅ Task submitted. ID = {}", taskName, taskId);

        //     pollUntilCompleted(taskName, taskId);
        //     // return CompletableFuture.completedFuture(null);
        // } catch (Exception e) {
        //     log.error("[#{}] ❌ Exception: {}", taskName, e.getMessage(), e);
        //     // failure.incrementAndGet();
        //     // return CompletableFuture.completedFuture(null);
        // }
    }
}

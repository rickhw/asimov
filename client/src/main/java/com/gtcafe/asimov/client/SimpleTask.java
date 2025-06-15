package com.gtcafe.asimov.client;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleTask implements Runnable {

    private final RestTemplate restTemplate = new RestTemplate();

    private TaskProperties props;

    private final String taskName;
    // private final int delayMillis;

    public SimpleTask(String name, TaskProperties props) {
        this.taskName = name;
        this.props = props;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        log.info("🟢 [{}] 開始 - 執行緒: {}", taskName, threadName);

        submitTask(taskName);
        // try {
        //     Thread.sleep(delayMillis);
        // } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        //     log.info("❌ [{}] 中斷: {} - 執行緒: {}", name, e.getMessage(), threadName);
        //     return;
        // }
        log.info("🔵 [{}] 完成 - 執行緒: {}", taskName, threadName);
    }

    public void submitTask(String taskName) {
        // log.info("index: [{}], message: [{}]", index, message);
        // total.incrementAndGet();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Request-Mode", "async");

            Map<String, String> body = Map.of("message", "Hello Asimov, message is: " + taskName);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    props.getHostname() + "/api/v1alpha/hello", request, Map.class);

            log.warn("[#{}] 回傳狀態: {}, ContentType: {}", taskName, response.getStatusCode(), response.getHeaders().getContentType());
            log.warn("[#{}] 回傳內容: {}", taskName, response.getBody());
            
            String taskId = (String) response.getBody().get("id");
            if (taskId == null) {
                log.error("[#{}] ❌ Failed to get Task ID", taskName);
                // failure.incrementAndGet();
                // return CompletableFuture.completedFuture(null);
            }

            log.info("[#{}] ✅ Task submitted. ID = {}", taskName, taskId);

            pollUntilCompleted(taskName, taskId);
            // return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("[#{}] ❌ Exception: {}", taskName, e.getMessage(), e);
            // failure.incrementAndGet();
            // return CompletableFuture.completedFuture(null);
        }
    }


    private void pollUntilCompleted(String name, String taskId) throws InterruptedException {
        while (true) {
            Thread.sleep(props.getPollIntervalMs());
            try {
                ResponseEntity<Map> response = restTemplate.getForEntity(
                        props.getHostname() + "/api/v1alpha/tasks/" + taskId, Map.class);

                String state = (String) response.getBody().get("state");
                log.info("[#{}] Task state: {}", name, state);

                if ("COMPLETED".equals(state)) {
                    // success.incrementAndGet();
                    log.info("[#{}] ✅ Task completed", name);
                    break;
                } else if ("FAILED".equals(state)) {
                    // failure.incrementAndGet();
                    log.info("[#{}] ❌ Task failed", name);
                    break;
                }
            } catch (Exception e) {
                log.warn("[#{}] Error polling task: {}", name, e.getMessage());
            }
        }
    }
}
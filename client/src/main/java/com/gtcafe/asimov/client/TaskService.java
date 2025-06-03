package com.gtcafe.asimov.client;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final TaskProperties props;

    public TaskService(TaskProperties props) {
        this.props = props;
    }

    public void runParallelTasks() {
        int count = ThreadLocalRandom.current().nextInt(1, props.getMaxRequest() + 1);
        log.info("🚀 Sending {} parallel requests", count);
        for (int i = 1; i <= count; i++) {
            String message = "Hello, Asimov #" + i;
            submitTask(i, message);
        }
    }

    @Async
    public void submitTask(int index, String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Request-Mode", "async");

            Map<String, String> body = Map.of("message", message);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    props.getHostname() + "/api/v1alpha/hello", request, Map.class);

            String taskId = (String) response.getBody().get("id");
            if (taskId == null) {
                log.error("[#{}] ❌ Failed to get Task ID", index);
                return;
            }

            log.info("[#{}] ✅ Task submitted. ID = {}", index, taskId);
            pollUntilCompleted(index, taskId);
        } catch (Exception e) {
            log.error("[#{}] ❌ Exception: {}", index, e.getMessage(), e);
        }
    }

    private void pollUntilCompleted(int index, String taskId) throws InterruptedException {
        while (true) {
            Thread.sleep(props.getPollIntervalMs());
            try {
                ResponseEntity<Map> response = restTemplate.getForEntity(
                        props.getHostname() + "/api/v1alpha/tasks/" + taskId, Map.class);

                String state = (String) response.getBody().get("state");
                log.info("[#{}] Task state: {}", index, state);

                if ("COMPLETED".equals(state)) {
                    log.info("[#{}] ✅ Task completed", index);
                    break;
                } else if ("FAILED".equals(state)) {
                    log.info("[#{}] ❌ Task failed", index);
                    break;
                }
            } catch (Exception e) {
                log.warn("[#{}] Error polling task: {}", index, e.getMessage());
            }
        }
    }
}

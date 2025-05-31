package com.gtcafe.asimov.client;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements ApplicationRunner {

    private final HelloClientService clientService;

    public Main(HelloClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 模擬發送 hello 請求
        clientService.postHelloMessage("Hello, Asimov");

        // 模擬查詢任務狀態
        String taskId = "30305161-53d5-41d8-9b30-eed67899801d";
        clientService.getTaskStatus(taskId);
    }
}

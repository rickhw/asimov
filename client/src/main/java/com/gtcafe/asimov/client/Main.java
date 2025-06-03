package com.gtcafe.asimov.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class Main  {

    @Autowired
    private TaskService taskService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    public void run() {
        taskService.runParallelTasks(); // 每次啟動執行一次
    }
}

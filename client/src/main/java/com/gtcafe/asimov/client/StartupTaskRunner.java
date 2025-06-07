// package com.gtcafe.asimov.client;

// import org.springframework.boot.context.event.ApplicationReadyEvent;
// import org.springframework.context.event.EventListener;
// import org.springframework.stereotype.Component;

// @Component
// public class StartupTaskRunner {

//     private final TaskService taskService;

//     public StartupTaskRunner(TaskService taskService) {
//         this.taskService = taskService;
//     }

//     @EventListener(ApplicationReadyEvent.class)
//     public void onApplicationReady() {
//         taskService.executeParallelTasks(); // 這時候 Spring AOP @Async 已生效
//     }
// }

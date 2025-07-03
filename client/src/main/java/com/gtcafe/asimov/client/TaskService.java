package com.gtcafe.asimov.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {

    private final Executor taskExecutor;
    private final TaskProperties props;
    private final TrafficSimulator trafficSimulator;

    public TaskService(@Qualifier("taskExecutor") Executor taskExecutor, TaskProperties props, TrafficSimulator trafficSimulator) {
        this.taskExecutor = taskExecutor;
        this.props = props;
        this.trafficSimulator = trafficSimulator;
    }

    @Scheduled(fixedRate = 30000)
    public void runScheduledTask() {
        log.info("🕐 Scheduled execution started");
        executeParallelTasks();
    }

    public CompletableFuture<Void> runTask(String name) {
        return CompletableFuture.runAsync(() -> {
            new SimpleTask(name, props).run();
        }, taskExecutor);
    }
   
    public void executeParallelTasks() {
        log.info("⚙️ 開始執行平行任務 - 主執行緒: " + Thread.currentThread().getName());

        int baseTaskCount = props.getMaxRequest();
        double multiplier = trafficSimulator.getCurrentTrafficMultiplier();
        int actualTaskCount = (int) (baseTaskCount * multiplier);

        log.info("Current traffic multiplier: {}, Actual tasks to run: {}", multiplier, actualTaskCount);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int index = 0; index < actualTaskCount; index++ ) {
            String name = String.format("Task #%s", index);
            futures.add(runTask(name));
        }

        // 等待所有任務完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        
        // CompletableFuture<Void> task1 = runTask("任務A");
        // CompletableFuture<Void> task2 = runTask("任務B");
        // CompletableFuture<Void> task3 = runTask("任務C");
        // CompletableFuture<Void> task4 = runTask("任務D");

        // CompletableFuture.allOf(task1, task2, task3, task4).join();

        System.out.println("✅ 所有任務完成");
    }

    // @Async("taskExecutor")
    // public CompletableFuture<Void> runTask(String name, int delayMillis) {
    //     new SimpleTask(name, delayMillis).run();
    //     return CompletableFuture.completedFuture(null);
    // }

    // public void executeParallelTasks() {
    //     System.out.println("⚙️ 開始執行平行任務");

    //     CompletableFuture<Void> task1 = runTask("任務A", 1000);
    //     CompletableFuture<Void> task2 = runTask("任務B", 1500);
    //     CompletableFuture<Void> task3 = runTask("任務C", 800);
    //     CompletableFuture<Void> task4 = runTask("任務D", 2000);

    //     CompletableFuture.allOf(task1, task2, task3, task4).join();

    //     System.out.println("✅ 所有任務完成");
    // }

}
    // @Scheduled(fixedRate = 60000)
    // public void runScheduledTask() {
    //     log.info("🕐 Scheduled execution started");
    //     runParallelTasks();
    // }

    // public void runParallelTasks() {
    //     int count = ThreadLocalRandom.current().nextInt(1, props.getMaxRequest() + 1);
    //     total.set(0);
    //     success.set(0);
    //     failure.set(0);
    //     long start = System.currentTimeMillis();

    //     log.info("🚀 Sending {} parallel requests...", count);
    //     List<CompletableFuture<Void>> futures = new ArrayList<>();

    //     for (int i = 1; i <= count; i++) {
    //         String message = "Hello, Asimov #" + i;
    //         futures.add(submitTask(i, message));
    //     }

    //     // 等待所有 async 任務完成
    //     CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    //     long duration = System.currentTimeMillis() - start;

    //     log.info("📊 Execution summary: total={}, success={}, failed={}, duration={}ms",
    //             total.get(), success.get(), failure.get(), duration);
    // }

// }

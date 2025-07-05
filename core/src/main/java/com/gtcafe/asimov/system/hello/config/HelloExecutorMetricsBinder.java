package com.gtcafe.asimov.system.hello.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.gtcafe.asimov.system.hello.HelloConstants;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class HelloExecutorMetricsBinder implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ThreadPoolTaskExecutor asyncExecutor;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ThreadPoolExecutor threadPool = asyncExecutor.getThreadPoolExecutor();

        Gauge.builder("executor.pool.size", threadPool, ThreadPoolExecutor::getPoolSize)
                .description("The current number of threads in the pool")
                .tag("name", HelloConstants.THREAD_POOL_EXECUTOR_BEANNAME)
                .register(meterRegistry);

        Gauge.builder("executor.active.count", threadPool, ThreadPoolExecutor::getActiveCount)
                .description("Active threads")
                .tag("name", HelloConstants.THREAD_POOL_EXECUTOR_BEANNAME)
                .register(meterRegistry);

        Gauge.builder("executor.queue.size", threadPool, e -> e.getQueue().size())
                .description("Task queue size")
                .tag("name", HelloConstants.THREAD_POOL_EXECUTOR_BEANNAME)
                .register(meterRegistry);

        Gauge.builder("executor.completed.count", threadPool, e -> e.getCompletedTaskCount())
                .description("Completed tasks")
                .tag("name", HelloConstants.THREAD_POOL_EXECUTOR_BEANNAME)
                .register(meterRegistry);
    }
}


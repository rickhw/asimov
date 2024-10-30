package com.gtcafe.asimov.consumer.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.platform.hello.SayHelloMessage;
import com.gtcafe.asimov.core.system.cache.CacheRepository;
import com.gtcafe.asimov.core.system.event.Event;
import com.gtcafe.asimov.core.system.event.EventHandler;
import com.gtcafe.asimov.core.system.task.TaskState;
import com.gtcafe.asimov.core.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloEventHandler implements EventHandler<SayHelloMessage> {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    CacheRepository cacheRepos;

    private static final int SIMULATE_DELAY = 10_000;

    @Override
    public void handleEvent(Event<SayHelloMessage> event) {
        try {
            log.info("Simulate the process, delay: [{}]", SIMULATE_DELAY);
            Thread.sleep(SIMULATE_DELAY);
            System.out.println("message: " + event.getData());

            // 更新狀態至 COMPLETED 並更新 cache
            event.transit(TaskState.COMPLETED);
            cacheRepos.saveOrUpdateObject(event.getId(), jsonUtils.modelToJsonString(event));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

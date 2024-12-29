package com.gtcafe.asimov.platform.hello.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.system.cache.CacheRepository;
import com.gtcafe.asimov.system.queue.model.EventHandler;
import com.gtcafe.asimov.system.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloEventHandler implements EventHandler<HelloEvent> {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    CacheRepository cacheRepos;

    private static final int SIMULATE_DELAY = 10_000;

    @Override
    public void handleEvent(HelloEvent event) {
        try {
            log.info("Simulate the process, delay: [{}]", SIMULATE_DELAY);
            Thread.sleep(SIMULATE_DELAY);
            System.out.println("message: " + event.getData());

            // 更新狀態至 COMPLETED 並更新 cache
            // event.transit(TaskState.COMPLETED);
            cacheRepos.saveOrUpdateObject(event.getEventId(), jsonUtils.modelToJsonString(event));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

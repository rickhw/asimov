package com.gtcafe.asimov.consumer.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.system.task.TaskState;
import com.gtcafe.asimov.core.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloEventHandler {

    @Autowired
    private JsonUtils _jsonUtils;

    @Autowired
    CacheRepository _cacheRepos;

    private static final int SIMULATE_DELAY = 10_000;

    public void handleSayHelloEvent(SayHelloEvent event) {

        try {
            log.info("Simulate the process, delay: [{}]", SIMULATE_DELAY);
            Thread.sleep(10000);
            System.out.println("message: " + event.getData());

            // TaskDomainObject tdo = event.getTask();
            event.transit(TaskState.COMPLETED);

            // 1.1 Update to cache
            String jsonString = _jsonUtils.modelToJsonString(event);
            _cacheRepos.saveOrUpdateObject(event.getId(), jsonString);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
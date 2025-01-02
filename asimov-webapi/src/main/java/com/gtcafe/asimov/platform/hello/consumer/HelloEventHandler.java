package com.gtcafe.asimov.platform.hello.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.task.TaskEventHandler;
import com.gtcafe.asimov.platform.task.schema.TaskState;
import com.gtcafe.asimov.system.cache.CacheRepository;
import com.gtcafe.asimov.system.constants.KindConstants;
import com.gtcafe.asimov.system.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloEventHandler implements TaskEventHandler<HelloEvent> {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private CacheRepository cacheRepos;

    private static final int SIMULATE_DELAY = 500;

    @Override
    public void handleEvent(HelloEvent event) {
        String cachedKey = String.format("%s:%s", KindConstants.PLATFORM_HELLO, event.getId());
        String taskCachedKeyForIndex = String.format("%s:%s", KindConstants.SYS_TASK, event.getId());
        try {
            // log.info("Simulate the process, delay: [{}]", SIMULATE_DELAY);
            
            // simulate the process
            log.info("task: [{}], state: [{}], data: [{}] ...: ", event.getId(), event.getState(), event.getData());
            // log.info("start the handler, cachedKey: [{}], state: [{}], sleep [{}], data: [{}] ...: ", cachedKey, event.getState(), SIMULATE_DELAY, event.getData());
            Thread.sleep(SIMULATE_DELAY);

            event.setState(TaskState.COMPLETED);
            log.info("task: [{}], state: [{}], data: [{}] ...: ", event.getId(), event.getState(), event.getData());
            // log.info("finish the handler, cachedKey: [{}], state: [{}], sleep [{}], data: [{}] ...: ", cachedKey, event.getState(), SIMULATE_DELAY, event.getData());

            String afterEventString = jsonUtils.modelToJsonString(event);

            cacheRepos.saveOrUpdateObject(cachedKey, afterEventString);
            cacheRepos.saveOrUpdateObject(taskCachedKeyForIndex, afterEventString);

        } catch (Exception ex) {
            event.setState(TaskState.FAILURE);
            cacheRepos.saveOrUpdateObject(cachedKey, jsonUtils.modelToJsonString(event));

            ex.printStackTrace();
        }
    }
}

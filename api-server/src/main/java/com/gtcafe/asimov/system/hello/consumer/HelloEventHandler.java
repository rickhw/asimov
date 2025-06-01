package com.gtcafe.asimov.system.hello.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.task.TaskEventHandler;
import com.gtcafe.asimov.system.task.schema.TaskState;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloEventHandler implements TaskEventHandler<HelloEvent> {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private CacheRepository cacheRepos;

    private static final int SIMULATE_DELAY = (int) (Math.random() * 1000000) % 5000;

    @Override
    public void handleEvent(HelloEvent event) {
        String cachedKey = String.format("%s:%s", KindConstants.PLATFORM_HELLO, event.getId());
        String taskCachedKeyForIndex = String.format("%s:%s", KindConstants.SYS_TASK, event.getId());
        try {
            // log.info("Simulate the process, delay: [{}]", SIMULATE_DELAY);
            
            // simulate the process
            log.info("start: task: [{}], state: [{}], data: [{}] ...: ", event.getId(), event.getState(), event.getData());
            // log.info("start the handler, cachedKey: [{}], state: [{}], sleep [{}], data: [{}] ...: ", cachedKey, event.getState(), SIMULATE_DELAY, event.getData());
            Thread.sleep(SIMULATE_DELAY);

            event.setState(TaskState.COMPLETED);
            // log.info("finish the handler, cachedKey: [{}], steate: [{}], sleep [{}], data: [{}] ...: ", cachedKey, event.getState(), SIMULATE_DELAY, event.getData());
            
            String afterEventString = jsonUtils.modelToJsonString(event);
            
            cacheRepos.saveOrUpdateObject(cachedKey, afterEventString);
            cacheRepos.saveOrUpdateObject(taskCachedKeyForIndex, afterEventString);

            log.info("finish: task: [{}], state: [{}], data: [{}] ...: ", event.getId(), event.getState(), event.getData());

        } catch (Exception ex) {
            event.setState(TaskState.FAILURE);
            cacheRepos.saveOrUpdateObject(cachedKey, jsonUtils.modelToJsonString(event));

            ex.printStackTrace();
        }
    }
}

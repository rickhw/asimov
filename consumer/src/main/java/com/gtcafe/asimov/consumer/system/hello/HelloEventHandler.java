package com.gtcafe.asimov.consumer.system.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.KindConstants;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.hello.HelloUtils;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
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

    // @Autowired
    // private HelloUtils _utils;

    @Override
    public boolean handleEvent(HelloEvent event) {
        String cachedKey = HelloUtils.renderCacheKey(event.getId());
        String taskCachedKeyForIndex = String.format("%s:%s", KindConstants.SYS_TASK, event.getId());
        int SIMULATE_DELAY = (int) (Math.random() * 10000);
        boolean result = false;
        try {
            // simulate the process
            log.info("start: task: [{}], state: [{}], delay: [{}] ... ", event.getId(), event.getState(), SIMULATE_DELAY);
            Thread.sleep(SIMULATE_DELAY);

            event.setState(TaskState.COMPLETED);
            // log.info("finish the handler, cachedKey: [{}], steate: [{}], sleep [{}], data: [{}] ...: ", cachedKey, event.getState(), SIMULATE_DELAY, event.getData());
            
            String afterEventString = jsonUtils.modelToJsonStringSafe(event).get();
            
            cacheRepos.saveOrUpdateObject(cachedKey, afterEventString);
            cacheRepos.saveOrUpdateObject(taskCachedKeyForIndex, afterEventString);

            log.info("finish: task: [{}], state: [{}], delay: [{}] ... ", event.getId(), event.getState(), SIMULATE_DELAY);

            result = true;

        } catch (Exception ex) {
            result = false;
            event.setState(TaskState.FAILURE);
            cacheRepos.saveOrUpdateObject(cachedKey, jsonUtils.modelToJsonStringSafe(event).get());

            ex.printStackTrace();
        }

        return result;
    }
}

package com.gtcafe.asimov.platform.tenant.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.system.cache.CacheRepository;
import com.gtcafe.asimov.system.constants.QueueName;
import com.gtcafe.asimov.system.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantConsumer {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private CacheRepository cacheRepos;

    @Autowired
    private TenantEventHandler eventHandler;

    @RabbitListener(queues = QueueName.TENANT_QUEUE)
    public  void consumeTenantQueue(String eventString) {
        TenantTaskEvent event = jsonUtils.jsonStringToModel(eventString, TenantTaskEvent.class);

        // 變更 Task 狀態至 RUNNING 並更新 cache
        // event.transit(TaskState.RUNNING);

        cacheRepos.saveOrUpdateObject(event.getId(), eventString);

        eventHandler.handleEvent(event);

        // 根據 data 類型取得相應的處理器並執行處理邏輯
        // SayHelloMessage data = event.getData();
        // EventHandler<T> handler = eventHandlerRegistry.getHandler(data.getClass());
        // if (handler != null) {
        //     handler.handleEvent(event);
        // } else {
        //     log.warn("No handler found for event type: {}", data.getClass());
        // }
    }

}


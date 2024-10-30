package com.gtcafe.asimov.consumer.platform;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.platform.tenant.RegisterTenantEvent;
import com.gtcafe.asimov.core.system.cache.CacheRepository;
import com.gtcafe.asimov.core.system.constants.QueueName;
import com.gtcafe.asimov.core.system.event.IMessage;
import com.gtcafe.asimov.core.system.task.TaskState;
import com.gtcafe.asimov.core.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlatformConsumer {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    CacheRepository cacheRepos;

    @Autowired
    HelloEventHandler eventHandler;

    @RabbitListener(queues = QueueName.SAY_HELLO)
    public <T extends IMessage> void receiveEvent(String eventString) {
        SayHelloEvent event = jsonUtils.jsonStringToModel(eventString, SayHelloEvent.class);

        // 變更 Task 狀態至 RUNNING 並更新 cache
        event.transit(TaskState.RUNNING);

        cacheRepos.saveOrUpdateObject(event.getId(), jsonUtils.modelToJsonString(event));

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

    @RabbitListener(queues = QueueName.REGISTER_TENANT)
    public void receiveTenantEvent(String eventString) {
        RegisterTenantEvent event = jsonUtils.jsonStringToModel(eventString, RegisterTenantEvent.class);

        // 變更 Task 狀態至 RUNNING 並更新 cache
        event.transit(TaskState.RUNNING);
        cacheRepos.saveOrUpdateObject(event.getId(), jsonUtils.modelToJsonString(event));

        // eventHandler.handleEvent(event);
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

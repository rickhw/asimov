package com.gtcafe.asimov.platform.tenant.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.common.utils.JsonUtils;
import com.gtcafe.asimov.core.platform.tenant.RegisterTenantEvent;
import com.gtcafe.asimov.core.system.cache.CacheRepository;
import com.gtcafe.asimov.core.system.constants.QueueName;
import com.gtcafe.asimov.platform.config.MessageProducer;
import com.gtcafe.asimov.platform.task.rest.response.RetrieveTaskResponse;
import com.gtcafe.asimov.platform.tenant.infrastructure.TenantEntity;
import com.gtcafe.asimov.platform.tenant.infrastructure.TenantRepository;
import com.gtcafe.asimov.platform.tenant.rest.request.RegisterTenantRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantService {

    @Autowired
    private MessageProducer producer;

    @Autowired
    private CacheRepository cacheRepos;

    @Autowired
    private TenantRepository repos;

    @Autowired
    private JsonUtils jsonUtils;

    public RetrieveTaskResponse registerTenantAsync(RegisterTenantRequest request) {
        RegisterTenantEvent event = new RegisterTenantEvent();
        event.getData().setId(request.getTenantKey());
        String taskJsonString = jsonUtils.modelToJsonString(event);

        // 1. send the event to the queue
        producer.sendEvent(event, QueueName.REGISTER_TENANT);

        // 2. store the task in the cache
        cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);

        // 3. store the tenant in the database
        TenantEntity entity = new TenantEntity();
        entity.setTenantKey(request.getTenantKey());
        entity.setDisplayName(request.getDisplayName());
        entity.setDescription(request.getDescription());
        entity.setRootAccount(request.getRootAccount());
        repos.save(entity);

        RetrieveTaskResponse res = new RetrieveTaskResponse(event);

        return res;
    }
}
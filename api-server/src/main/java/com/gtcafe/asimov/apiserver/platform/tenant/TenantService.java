package com.gtcafe.asimov.apiserver.platform.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.tenant.operation.RegisterTenantRequest;
import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.constants.QueueName;
import com.gtcafe.asimov.core.platform.tenant.RegisterTenantEvent;
import com.gtcafe.asimov.core.utils.JsonUtils;

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

    public RegisterTenantEvent registerTenantAsync(RegisterTenantRequest request) {
        RegisterTenantEvent event = new RegisterTenantEvent();
        event.getData().setId(request.getTenantKey());

        producer.sendEvent(event, QueueName.REGISTER_TENANT);

        String taskJsonString = jsonUtils.modelToJsonString(event);
        cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);

        TenantEntity entity = new TenantEntity();
        entity.setTenantKey(request.getTenantKey());
        entity.setDisplayName(request.getDisplayName());
        entity.setDescription(request.getDescription());
        entity.setRootAccount(request.getRootAccount());
        repos.save(entity);

        return event;
    }
}
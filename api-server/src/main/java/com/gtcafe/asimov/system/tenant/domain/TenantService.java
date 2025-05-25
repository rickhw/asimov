package com.gtcafe.asimov.system.tenant.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.constants.QueueName;
import com.gtcafe.asimov.framework.utils.JsonUtils;
import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.infrastructure.queue.Producer;
import com.gtcafe.asimov.rest.backstage.tenant.request.RegisterTenantRequest;
import com.gtcafe.asimov.rest.backstage.tenant.response.TenantTaskResponse;
import com.gtcafe.asimov.system.tenant.TenantMapper;
import com.gtcafe.asimov.system.tenant.consumer.TenantTaskEvent;
import com.gtcafe.asimov.system.tenant.model.Tenant;
import com.gtcafe.asimov.system.tenant.repository.TenantEntity;
import com.gtcafe.asimov.system.tenant.repository.TenantRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantService {

    @Autowired
    private Producer producer;

    @Autowired
    private CacheRepository cacheRepos;

    @Autowired
    private TenantRepository repos;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private TenantMapper mapper;

    public TenantTaskResponse registerTenantAsync(RegisterTenantRequest request) {

        // create a domain object
        Tenant tenant = mapper.mapRequestToDomain(request);

        // create a taskObject
        TenantTaskResponse res = new TenantTaskResponse();
        res.setData(tenant);
        
        TenantTaskEvent event = new TenantTaskEvent(tenant);
        String taskJsonString = jsonUtils.modelToJsonString(event);
        
        //////////////////////////////
        /// persist

        // 1. send the event to the queue
        producer.sendEvent(event, QueueName.TENANT_QUEUE);

        // 2. store the task in the cache
        cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);

        // 3. store the tenant in the database
        TenantEntity entity = mapper.mapTenantEntity(request);
        repos.save(entity);

        return res;
    }
}
package com.gtcafe.asimov.platform.tenant.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.conifg.MessageProducer;
import com.gtcafe.asimov.core.platform.tenant.Tenant;
import com.gtcafe.asimov.core.platform.tenant.TenantEvent;
import com.gtcafe.asimov.core.system.cache.CacheRepository;
import com.gtcafe.asimov.core.system.constants.QueueName;
import com.gtcafe.asimov.core.system.utils.JsonUtils;
import com.gtcafe.asimov.platform.tenant.TenantMapper;
import com.gtcafe.asimov.platform.tenant.infrastructure.TenantEntity;
import com.gtcafe.asimov.platform.tenant.infrastructure.TenantRepository;
import com.gtcafe.asimov.platform.tenant.rest.request.RegisterTenantRequest;
import com.gtcafe.asimov.platform.tenant.rest.response.TenantTaskResponse;

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

    @Autowired
    private TenantMapper mapper;

    public TenantTaskResponse registerTenantAsync(RegisterTenantRequest request) {

        // create a domain object
        Tenant tenant = mapper.mapRequestToDomain(request);

        // create a taskObject
        TenantTaskResponse res = new TenantTaskResponse();
        res.setData(tenant);
        
        TenantEvent event = new TenantEvent(tenant);
        String taskJsonString = jsonUtils.modelToJsonString(event);
        
        //////////////////////////////
        /// persist

        // 1. send the event to the queue
        producer.sendEvent(event, QueueName.REGISTER_TENANT);

        // 2. store the task in the cache
        cacheRepos.saveOrUpdateObject(event.getEventId(), taskJsonString);

        // 3. store the tenant in the database
        TenantEntity entity = mapper.mapTenantEntity(request);
        repos.save(entity);

        

        return res;
    }
}
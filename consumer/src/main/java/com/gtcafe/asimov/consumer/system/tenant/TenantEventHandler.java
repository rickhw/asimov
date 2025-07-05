package com.gtcafe.asimov.consumer.system.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.infrastructure.cache.CacheRepository;
import com.gtcafe.asimov.system.task.TaskEventHandler;
import com.gtcafe.asimov.system.tenant.model.TenantTaskEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantEventHandler implements TaskEventHandler<TenantTaskEvent> {

    @Autowired
    private CacheRepository cacheRepos;

    @Override
    public boolean handleEvent(TenantTaskEvent event) {
        return false;
    }
}

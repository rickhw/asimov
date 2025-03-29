package com.gtcafe.asimov.platform.tenant.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.task.TaskEventHandler;
import com.gtcafe.asimov.system.cache.CacheRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantEventHandler implements TaskEventHandler<TenantTaskEvent> {

    @Autowired
    private CacheRepository cacheRepos;

    @Override
    public void handleEvent(TenantTaskEvent event) {

    }
}

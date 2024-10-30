package com.gtcafe.asimov.consumer.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.platform.tenant.RegisterTenantMessage;
import com.gtcafe.asimov.core.system.cache.CacheRepository;
import com.gtcafe.asimov.core.system.event.Event;
import com.gtcafe.asimov.core.system.event.EventHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantEventHandler implements EventHandler<RegisterTenantMessage> {

    @Autowired
    CacheRepository cacheRepos;

    @Override
    public void handleEvent(Event<RegisterTenantMessage> event) {

    }
}

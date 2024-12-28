package com.gtcafe.asimov.platform.tenant;

import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.tenant.infrastructure.TenantEntity;
import com.gtcafe.asimov.platform.tenant.model.Tenant;
import com.gtcafe.asimov.platform.tenant.rest.request.RegisterTenantRequest;

@Service
public class TenantMapper {

    public Tenant mapRequestToDomain(RegisterTenantRequest request) {
        Tenant tenant = new Tenant();
        tenant.getIdentifier().setTenantKey(request.getTenantKey());
        tenant.getSpec().setDisplayName(request.getDisplayName());
        tenant.getSpec().setDescription(request.getDescription());
        tenant.getSpec().setRootAccount(request.getRootAccount());
        return tenant;
    }

    public TenantEntity mapTenantEntity(RegisterTenantRequest request) {
        TenantEntity entity = new TenantEntity();
        entity.setTenantKey(request.getTenantKey());
        entity.setDisplayName(request.getDisplayName());
        entity.setDescription(request.getDescription());
        entity.setRootAccount(request.getRootAccount());
        return entity;
    }

}

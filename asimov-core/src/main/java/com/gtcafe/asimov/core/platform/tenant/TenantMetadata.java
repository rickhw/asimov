package com.gtcafe.asimov.core.platform.tenant;

import com.gtcafe.asimov.core.system.event2.Metadata;

import lombok.Getter;
import lombok.Setter;

// public class TenantMetadata extends Metadata<TenantState> {
public class TenantMetadata extends Metadata {

    @Getter @Setter
    private String tenantId;

    public TenantMetadata() {
        super();
        this.tenantId = "tenant-" + getId();
        setState(TenantState.PENDING); // 預設為 PENDING 狀態
    }
}

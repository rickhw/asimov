package com.gtcafe.asimov.core.system.model;

import lombok.Getter;
import lombok.Setter;

public class TenantContext {
    @Getter @Setter
    private String tenantId;

    @Getter @Setter
    private String tenantKey;

    @Getter @Setter
    private String tenantName;

    @Getter @Setter
    private String description;
}

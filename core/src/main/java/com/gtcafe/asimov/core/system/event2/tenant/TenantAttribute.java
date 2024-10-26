package com.gtcafe.asimov.core.system.event2.tenant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// public class TenantAttribute extends Attribute {
@NoArgsConstructor
public class TenantAttribute {

    @Getter @Setter
    private String displayName;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private String rootAccount;

}

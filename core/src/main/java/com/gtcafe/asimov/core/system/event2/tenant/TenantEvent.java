package com.gtcafe.asimov.core.system.event2.tenant;

import com.gtcafe.asimov.core.constants.GeneralConstants;
import com.gtcafe.asimov.core.constants.KindConstants;
import com.gtcafe.asimov.core.system.event2.Event;

public class TenantEvent extends Event<TenantMetadata, TenantAttribute> {

    public TenantEvent() {
        super.setKind(KindConstants.PLATFORM_TENANT);
        super.setApiVersion(GeneralConstants.CURRENT_API_VERSION);
        this.setMetadata(new TenantMetadata());
        this.setAttributes(new TenantAttribute());
    }
}

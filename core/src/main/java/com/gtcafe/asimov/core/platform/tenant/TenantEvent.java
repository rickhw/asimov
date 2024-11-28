package com.gtcafe.asimov.core.platform.tenant;

import com.gtcafe.asimov.system.constants.GeneralConstants;
import com.gtcafe.asimov.system.constants.KindConstants;
import com.gtcafe.asimov.system.event2.Event;

public class TenantEvent extends Event<TenantMetadata, TenantAttribute> {

    public TenantEvent() {
        super.setKind(KindConstants.PLATFORM_TENANT);
        super.setApiVersion(GeneralConstants.CURRENT_API_VERSION);
        this.setMetadata(new TenantMetadata());
        this.setAttributes(new TenantAttribute());
    }
}

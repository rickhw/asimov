package com.gtcafe.asimov.platform.tenant.domain.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.gtcafe.asimov.platform.tenant.model.State;
import com.gtcafe.asimov.platform.tenant.model.Tenant;
import com.gtcafe.asimov.system.constants.KindEnum;

public class TenantTest {

    @Test
    void testTenantDomainObjectCreation() {
        Tenant tenant = new Tenant();

        // Assert
        assertNotNull(tenant);  // 檢查 tenant 物件是否成功生成

        // kind 是否正確
        assertEquals(KindEnum.PLATFORM_TENANT, tenant.getKind());

        // 檢查 KeySet 是否生成
        assertNotNull(tenant.getIdentifier().getResourceId());  

        // 檢查 metadata 是否生成
        assertNotNull(tenant.getMetadata());  
        assertNotNull(tenant.getMetadata().getCreationTime());
        assertNotNull(tenant.getMetadata().getLastModified());
        assertEquals(State.PENDING, tenant.getMetadata().getState());  // 檢查 state 初始值

        // 檢查 spec 是否生成
        assertNotNull(tenant.getSpec());  

    }
}

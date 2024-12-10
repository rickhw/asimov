package com.gtcafe.asimov.platform.tenant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.gtcafe.asimov.platform.tenant.domain.model.State;
import com.gtcafe.asimov.platform.tenant.domain.model.Tenant;
import com.gtcafe.asimov.core.system.constants.KindConstants;

public class TenantTest {

    @Test
    void testTenantDomainObjectCreation() {
        // Arrange & Act
        Tenant tenant = new Tenant();

        // Assert
        assertNotNull(tenant);  // 檢查 tenant 物件是否成功生成
        assertNotNull(tenant.getMetadata());  // 檢查 metadata 是否生成
        assertNotNull(tenant.getSpec());  // 檢查 spec 是否生成
        assertNotNull(tenant.getMetadata().get_resourceId());  // 檢查 resourceId 是否生成
        assertEquals(State.PENDING, tenant.getMetadata().get_state());  // 檢查 state 初始值

        // Optional: 檢查其他屬性的初始值，如 kind 是否正確
        assertEquals(KindConstants.PLATFORM_TENANT, tenant.getKind());
    }
}

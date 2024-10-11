package com.gtcafe.asimov.apiserver.platform.tenant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.gtcafe.asimov.core.platform.PlatformKind;

public class TenantDomainObjectTest {

    @Test
    void testTenantDomainObjectCreation() {
        // Arrange & Act
        TenantDomainObject tenant = new TenantDomainObject();

        // Assert
        assertNotNull(tenant);  // 檢查 tenant 物件是否成功生成
        assertNotNull(tenant.getMetadata());  // 檢查 metadata 是否生成
        assertNotNull(tenant.getSpec());  // 檢查 spec 是否生成
        assertNotNull(tenant.getMetadata()._resourceId);  // 檢查 resourceId 是否生成
        assertEquals(TenantDomainObject.State.PENDING, tenant.getMetadata().get_state());  // 檢查 state 初始值

        // Optional: 檢查其他屬性的初始值，如 kind 是否正確
        assertEquals(PlatformKind.PLATFORM_TENANT, tenant.getKind());
    }
}

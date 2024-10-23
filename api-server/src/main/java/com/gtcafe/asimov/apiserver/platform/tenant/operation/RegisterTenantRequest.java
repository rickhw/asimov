package com.gtcafe.asimov.apiserver.platform.tenant.operation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterTenantRequest {

	@NotBlank(message = "tenantKey cannot empty")
    @Size(max = 50, message = "tenantKey max lenght is 50.")
    private String tenantKey;

	@NotBlank(message = "rootAccount cannot empty")
    @Size(max = 50, message = "rootAccount max lenght is 50.")
    private String rootAccount;

    @NotBlank(message = "description cannot empty")
    @Size(max = 255, message = "description max lenght is 255.")
    private String description;


}

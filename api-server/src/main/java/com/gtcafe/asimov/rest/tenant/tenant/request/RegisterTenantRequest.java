package com.gtcafe.asimov.rest.tenant.tenant.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class RegisterTenantRequest {

	@NotBlank(message = "tenantKey cannot empty")
    @Size(max = 50, message = "tenantKey max lenght is 50.")
    @Getter @Setter
    private String tenantKey;

    @NotBlank(message = "displayName cannot empty")
    @Size(max = 50, message = "displayName max lenght is 50.")
    @Getter @Setter
    private String displayName;

	@NotBlank(message = "rootAccount cannot empty")
    @Size(max = 50, message = "rootAccount max lenght is 50.")
    @Getter @Setter
    private String rootAccount;

    @NotBlank(message = "description cannot empty")
    @Size(max = 255, message = "description max lenght is 255.")
    @Getter @Setter
    private String description;

}

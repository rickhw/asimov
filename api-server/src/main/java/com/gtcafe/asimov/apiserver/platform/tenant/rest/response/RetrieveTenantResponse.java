package com.gtcafe.asimov.apiserver.platform.tenant.rest.response;

import lombok.Getter;
import lombok.Setter;

public class RetrieveTenantResponse {

	@Getter @Setter
	private Long id;

	@Getter @Setter
    private String accountName;

	@Getter @Setter
	private String state;

	@Getter @Setter
	private String description;

}

package com.gtcafe.asimov.apiserver.platform.tenant.domain.model;

import com.gtcafe.asimov.system.constants.KindConstants;

import lombok.Getter;
import lombok.Setter;

public class Tenant {

	@Setter @Getter
	private String kind;

	@Setter @Getter
	private Metadata metadata;

	@Setter @Getter
	private Spec spec;

	public Tenant() {
		this.kind = KindConstants.PLATFORM_TENANT;
		this.metadata = new Metadata();
		this.spec = new Spec();
	}
}



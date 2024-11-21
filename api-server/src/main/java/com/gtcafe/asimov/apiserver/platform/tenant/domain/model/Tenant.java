package com.gtcafe.asimov.apiserver.platform.tenant.domain.model;

import com.gtcafe.asimov.core.system.constants.KindConstants;

public class Tenant {

	private String kind;
	private Metadata metadata;
	private Spec spec;

	public Tenant() {
		this.kind = KindConstants.PLATFORM_TENANT;
		this.metadata = new Metadata();
		this.spec = new Spec();
	}

	public String getKind() {
		return kind;
	}

	// public void setKind(String kind) {
	// 	this.kind = kind;
	// }

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Spec getSpec() {
		return spec;
	}

	public void setSpec(Spec spec) {
		this.spec = spec;
	}
}


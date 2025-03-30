package com.gtcafe.asimov.platform.tenant.model;

import com.gtcafe.asimov.framework.constants.KindEnum;
import com.gtcafe.asimov.framework.constants.VersionEnum;

import lombok.Getter;
import lombok.Setter;

public class Tenant {

	@Getter
	@Setter
	private VersionEnum apiVersion;

	@Setter
	@Getter
	private KindEnum kind;

	@Setter
	@Getter
	private Identifier identifier;

	@Setter
	@Getter
	private Metadata metadata;

	@Setter
	@Getter
	private Spec spec;

	public Tenant() {
		this.kind = KindEnum.PLATFORM_TENANT;
		this.apiVersion = VersionEnum.V1_ALPHA;
		this.identifier = new Identifier();
		this.metadata = new Metadata();
		this.spec = new Spec();
	}
}

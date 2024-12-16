package com.gtcafe.asimov.core.platform.tenant;

import com.gtcafe.asimov.core.system.constants.KindEnum;
import com.gtcafe.asimov.core.system.constants.VersionEnum;

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

package com.gtcafe.asimov.core.system.constants;

import lombok.Getter;

public enum KindEnum {

	PLATFORM_HELLO("platform.Hello"),
	PLATFORM_TENANT("platform.Tenant"),
	PLATFORM_TASK("platform.Task")

	;

	@Getter
	private String value;

	KindEnum(String value) {
		this.value = value;
	}


}


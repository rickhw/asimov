package com.gtcafe.asimov.core.system.constants;

import lombok.Getter;

public enum VersionEnum {

	V1_ALPHA("v1alpha"),
	V1_BETA("v1beta"),
	V1("v1")

	;

	@Getter
	private String value;

	VersionEnum(String value) {
		this.value = value;
	}


}


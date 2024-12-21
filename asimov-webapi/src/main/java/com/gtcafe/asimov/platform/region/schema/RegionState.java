package com.gtcafe.asimov.platform.region.schema;

public enum RegionState {
	AVAILABLE("available"),
	DELETED("deleted")
	;

	private String value;

	RegionState(String value) {
		this.value = value;
	}
}

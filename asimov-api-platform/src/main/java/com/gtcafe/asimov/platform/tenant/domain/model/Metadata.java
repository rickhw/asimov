package com.gtcafe.asimov.platform.tenant.domain.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public 
class Metadata {
    /* write once, read manay by user */
    @Getter @Setter
    String tenantKey; // "this-is-a-tenant",

    /*  write once, read many by system */
    @Getter @Setter
    String _apiVersion; // "v1alpha"

    @Getter @Setter
    String _resourceId; // "75ac3d12-d237-4a81-8173-049e948906d4"

    @Getter @Setter
    String _tenantId; 	// "t-1234567890"

    @Getter @Setter
    State _state;		// "pending"

    @Getter @Setter
    String _creationTime;	// "2021-12-10T00:29:06.800+08:00",

    @Getter @Setter
    String _lastModified;	// "2021-12-10T00:29:06.800+08:00"

    Metadata() {
        this._resourceId = UUID.randomUUID().toString();
        this._state = State.PENDING;
    }

}
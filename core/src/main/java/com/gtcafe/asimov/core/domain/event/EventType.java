package com.gtcafe.asimov.core.domain.event;

public enum EventType {
    // Domain
    CREATE_CONTAINER,
    DELETE_CONTAINER,

    REGISTER_TENANT,
    DEREGISTER_TENANT,

    // platform
    SAY_HELLO,

    // system
    ASYNC_TASK,
}

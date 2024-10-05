package com.gtcafe.asimov.core.event;

public enum EventType {
    CREATE_CONTAINER,
    DELETE_CONTAINER,

    REGISTER_TENANT,
    DEREGISTER_TENANT,

    // platform
    SAY_HELLO,

    // system
    ASYNC_TASK,
}

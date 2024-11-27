package com.gtcafe.asimov.core.system.context;

import lombok.Getter;
import lombok.Setter;

public class ApiMetadataContext {
    @Getter @Setter
    private String operationId;

    @Getter @Setter
    private String method;

    @Getter @Setter
    private String uri;

    @Getter @Setter
    private String kind; 
}

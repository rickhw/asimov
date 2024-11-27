package com.gtcafe.asimov.core.common.bean;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class HttpRequestContextBean {
    
    public RequestId getRequestId() {
        return new RequestId();
    }
}

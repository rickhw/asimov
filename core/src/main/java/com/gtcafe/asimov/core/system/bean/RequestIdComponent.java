package com.gtcafe.asimov.core.system.bean;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestIdComponent {
    
    public RequestIdBean getRequestId() {
        return new RequestIdBean();
    }
}

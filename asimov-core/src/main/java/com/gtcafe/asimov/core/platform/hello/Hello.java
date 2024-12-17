package com.gtcafe.asimov.core.platform.hello;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class Hello {

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String timestamp;

}

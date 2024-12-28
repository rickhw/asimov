package com.gtcafe.asimov.platform.hello.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class Hello {

    @Getter
    @Setter
    private String message;

    // @Getter
    // @Setter
    // private String timestamp;

}

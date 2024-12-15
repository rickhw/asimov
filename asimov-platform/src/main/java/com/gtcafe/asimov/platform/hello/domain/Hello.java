package com.gtcafe.asimov.platform.hello.domain;

import lombok.Getter;
import lombok.Setter;

public class Hello {
    
    @Getter @Setter
    private String message;

    public Hello(String message) {
        this.message = message;
    }

    public Hello() {
        this("Hello, World!");
    }

}

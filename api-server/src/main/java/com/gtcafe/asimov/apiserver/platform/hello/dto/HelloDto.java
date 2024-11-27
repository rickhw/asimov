package com.gtcafe.asimov.apiserver.platform.hello.dto;

import jakarta.validation.constraints.NotBlank;

public class HelloDto {

    @NotBlank(message = "message cannot empty")
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

package com.gtcafe.asimov.apiserver.platform.hello.operation;

import java.util.Date;

public class HelloResponse {

    private String message;
    private String launchTime;

    public HelloResponse(String message) {
        this.message = message;
        this.launchTime = new Date().toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getLaunchTime() {
        return this.launchTime;
    }

}

package com.gtcafe.asimov.core.platform.tenant;

import com.gtcafe.asimov.core.event.IMessage;

public class CreateTenantMessage implements IMessage {
    private final String content;

    public CreateTenantMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}

package com.gtcafe.asimov.core.platform.tenant;

import com.gtcafe.asimov.core.event.IMessage;

public class DeleteTenantMessage implements IMessage {
    private final String content;

    public DeleteTenantMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}

package com.gtcafe.asimov.core.domain.event;

public interface IMessage {
    String getKind();
    String getId();
    String getData();
}
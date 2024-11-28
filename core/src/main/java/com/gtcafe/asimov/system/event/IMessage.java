package com.gtcafe.asimov.system.event;

public interface IMessage {
    String getKind();
    String getId();
    String getData();
}
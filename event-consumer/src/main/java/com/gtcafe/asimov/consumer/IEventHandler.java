package com.gtcafe.asimov.consumer;

import com.example.common.event.Message;

// 定义 IEventHandler 接口
public interface IEventHandler<T extends Message> {
    void handle(T message);
}

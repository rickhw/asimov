package com.gtcafe.asimov.core.platform.hello;

import java.util.UUID;

import com.gtcafe.asimov.core.domain.event.EventType;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;

public class HelloEventV3  {
    private String id;
    private EventType eventType;
    private TaskDomainObject task;
    private SayHelloMessage data;

    public HelloEventV3(String id, EventType eventType, SayHelloMessage data) {
        this.id = id;
        this.eventType = eventType;
        this.task = new TaskDomainObject();
        this.data = data;
    }

    public HelloEventV3(SayHelloMessage data) {
        this.id = UUID.randomUUID().toString();
        this.task = new TaskDomainObject();
        this.eventType = EventType.SAY_HELLO;
        this.data = data;
    }

    public HelloEventV3() {
        this.id = UUID.randomUUID().toString();
        this.task = new TaskDomainObject();
        this.eventType = EventType.SAY_HELLO;
        this.data = null;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public TaskDomainObject getTask() {
        return task;
    }

    public void setTask(TaskDomainObject task) {
        this.task = task;
    }

    public SayHelloMessage getData() {
        return data;
    }

    public void setData(SayHelloMessage data) {
        this.data = data;
    }

    public String toString() {
        return String.format("HelloEventV3: id: [%s], eventType: [%s], task: [%s], data: [%s]", id, eventType, task, data);
    }
}
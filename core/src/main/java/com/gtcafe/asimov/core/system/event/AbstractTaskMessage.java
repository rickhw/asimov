package com.gtcafe.asimov.core.system.event;

import java.util.UUID;

import com.gtcafe.asimov.core.system.task.TaskDomainObject;


public abstract class AbstractTaskMessage<T> {

	private String id;
	private TaskDomainObject task;
	private T data;

	public AbstractTaskMessage(T data) {
		this.id = UUID.randomUUID().toString();
		this.task = new TaskDomainObject();
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TaskDomainObject getTask() {
		return task;
	}

	public void setMetadata(TaskDomainObject task) {
		this.task = task;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}


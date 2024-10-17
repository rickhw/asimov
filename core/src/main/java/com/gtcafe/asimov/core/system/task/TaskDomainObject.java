package com.gtcafe.asimov.core.system.task;

import java.util.UUID;

import com.gtcafe.asimov.core.constants.Kind;

// POJO: w/o any anotation.

public class TaskDomainObject {

	private String kind;
	private String taskId;
	private TaskMetadata metadata;
	private Object spec;

	public TaskDomainObject() {
		this.taskId = UUID.randomUUID().toString();
		this.kind = Kind.SYS_TASK;
		this.metadata = new TaskMetadata();
		// this.spec = new Spec();
	}

	public String getTaskId() {
		return taskId;
	}

	public String getKind() {
		return kind;
	}

	public TaskMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(TaskMetadata metadata) {
		this.metadata = metadata;
	}

	public Object getSpec() {
		return spec;
	}

	public void setSpec(Object spec) {
		this.spec = spec;
	}
}


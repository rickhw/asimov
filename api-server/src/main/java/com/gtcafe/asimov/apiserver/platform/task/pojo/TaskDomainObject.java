package com.gtcafe.asimov.apiserver.platform.task.pojo;

import java.util.UUID;

import com.gtcafe.asimov.apiserver.platform.task.TaskConstants;

// POJO: w/o any anotation.

// {
//     "taskId": "75ac3d12-d237-4a81-8173-049e948906d4",
//     "state": "pending",
//     "kind": "core.Hello",
//     "operationId": "sayHello",
//     "data": {
//         "kind": "core.Hello",
//         "metadata": {
//             "_apiVersion": "v1alpha",
//             "_state": "pending",
//             "_creationTime": "2021-12-10T00:29:06.800+08:00",
//             "_lastModified": "2021-12-10T00:29:06.800+08:00"
//         },
//         "spec": {
//             "message": "Hello, Master Asimov"
//         }
//     }
// }


public class TaskDomainObject {

	private String kind;
	private String taskId;
	private TaskMetadata metadata;
	private Object spec;

	public TaskDomainObject() {
		this.taskId = UUID.randomUUID().toString();
		this.kind = TaskConstants.KIND_NAME;
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


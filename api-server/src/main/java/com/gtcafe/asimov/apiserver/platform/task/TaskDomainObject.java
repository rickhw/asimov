package com.gtcafe.asimov.apiserver.platform.task;

import java.util.UUID;

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
	private Metadata metadata;
	private Spec spec;

	public TaskDomainObject() {
		this.kind = TaskConstants.KIND_NAME;
		this.metadata = new Metadata();
		this.spec = new Spec();
	}

	public String getKind() {
		return kind;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Spec getSpec() {
		return spec;
	}

	public void setSpec(Spec spec) {
		this.spec = spec;
	}

	class Metadata {
		/* write once, read manay by user */


		/*  write once, read many by system */
        String _apiVersion; // "v1alpha"
        State _state;		// "pending"
        String _creationTime;	// "2021-12-10T00:29:06.800+08:00",
        String _lastModified;	// "2021-12-10T00:29:06.800+08:00"

		Metadata() {
			// this._resourceId = UUID.randomUUID().toString();
			this._state = State.PENDING;
		}

		public String get_apiVersion() {
			return _apiVersion;
		}

		public void set_apiVersion(String _apiVersion) {
			this._apiVersion = _apiVersion;
		}

		// public String get_resourceId() {
		// 	return _resourceId;
		// }

		// public void set_resourceId(String _resourceId) {
		// 	this._resourceId = _resourceId;
		// }


		public State get_state() {
			return _state;
		}

		public void set_state(State _state) {
			this._state = _state;
		}

		public String get_creationTime() {
			return _creationTime;
		}

		public void set_creationTime(String _creationTime) {
			this._creationTime = _creationTime;
		}

		public String get_lastModified() {
			return _lastModified;
		}

		public void set_lastModified(String _lastModified) {
			this._lastModified = _lastModified;
		}


	}

	class Spec {
        String message;

		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}

	}

	enum State {
		PENDING,
		RUNNING,
		COMPLETED
	}

}


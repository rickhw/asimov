package com.gtcafe.asimov.apiserver.platform.tenant;

import java.util.UUID;

import com.gtcafe.asimov.core.constants.Kind;
// POJO: w/o any anotation.

// {
//     "kind": "core.Tenant",
//     "metadata": {
//         "tenantKey": "this-is-a-tenant",    // write once, read manay by user
//         "_apiVersion": "v1alpha",           // write once, read manay by system
//         "_resourceId": "75ac3d12-d237-4a81-8173-049e948906d4",
//         "_tenantId": "t-1234567890",
//         "_state": "pending",
//         "_creationTime": "2021-12-10T00:29:06.800+08:00",
//         "_lastModified": "2021-12-10T00:29:06.800+08:00"
//     },
//     "spec": {
//         "rootAccount": "rick@abc.com",
//         "description": "this is a tenant"
//     }
// }


public class TenantDomainObject {

	private String kind;
	private Metadata metadata;
	private Spec spec;

	public TenantDomainObject() {
		this.kind = Kind.PLATFORM_TENANT;
		this.metadata = new Metadata();
		this.spec = new Spec();
	}

	public String getKind() {
		return kind;
	}

	// public void setKind(String kind) {
	// 	this.kind = kind;
	// }

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
        String tenantKey; // "this-is-a-tenant",

		/*  write once, read many by system */
        String _apiVersion; // "v1alpha"
        String _resourceId; // "75ac3d12-d237-4a81-8173-049e948906d4"
        String _tenantId; 	// "t-1234567890"
        State _state;		// "pending"
        String _creationTime;	// "2021-12-10T00:29:06.800+08:00",
        String _lastModified;	// "2021-12-10T00:29:06.800+08:00"

		Metadata() {
			this._resourceId = UUID.randomUUID().toString();
			this._state = State.PENDING;
		}

		public String getTenantKey() {
			return tenantKey;
		}

		public void setTenantKey(String tenantKey) {
			this.tenantKey = tenantKey;
		}

		public String get_apiVersion() {
			return _apiVersion;
		}

		public void set_apiVersion(String _apiVersion) {
			this._apiVersion = _apiVersion;
		}

		public String get_resourceId() {
			return _resourceId;
		}

		public void set_resourceId(String _resourceId) {
			this._resourceId = _resourceId;
		}

		public String get_tenantId() {
			return _tenantId;
		}

		public void set_tenantId(String _tenantId) {
			this._tenantId = _tenantId;
		}

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
        String rootAccount; 	// "rick@abc.com"
        String description;		// "this is a tenant"
		public String getRootAccount() {
			return rootAccount;
		}
		public void setRootAccount(String rootAccount) {
			this.rootAccount = rootAccount;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

	}

	enum State {
		PENDING,
		RUNNING,
		STARTING,
		SHUTTING_DOWN,
		TERMINATED
	}

}


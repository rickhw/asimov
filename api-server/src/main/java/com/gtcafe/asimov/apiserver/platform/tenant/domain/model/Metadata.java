package com.gtcafe.asimov.apiserver.platform.tenant.domain.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public 
class Metadata {
    /* write once, read manay by user */
    @Getter @Setter
    String tenantKey; // "this-is-a-tenant",

    /*  write once, read many by system */
    @Getter @Setter
    String _apiVersion; // "v1alpha"

    @Getter @Setter
    String _resourceId; // "75ac3d12-d237-4a81-8173-049e948906d4"

    @Getter @Setter
    String _tenantId; 	// "t-1234567890"

    @Getter @Setter
    State _state;		// "pending"

    @Getter @Setter
    String _creationTime;	// "2021-12-10T00:29:06.800+08:00",

    @Getter @Setter
    String _lastModified;	// "2021-12-10T00:29:06.800+08:00"

    Metadata() {
        this._resourceId = UUID.randomUUID().toString();
        this._state = State.PENDING;
    }

    // public String getTenantKey() {
    //     return tenantKey;
    // }

    // public void setTenantKey(String tenantKey) {
    //     this.tenantKey = tenantKey;
    // }

    // public String get_apiVersion() {
    //     return _apiVersion;
    // }

    // public void set_apiVersion(String _apiVersion) {
    //     this._apiVersion = _apiVersion;
    // }

    // public String get_resourceId() {
    //     return _resourceId;
    // }

    // public void set_resourceId(String _resourceId) {
    //     this._resourceId = _resourceId;
    // }

    // public String get_tenantId() {
    //     return _tenantId;
    // }

    // public void set_tenantId(String _tenantId) {
    //     this._tenantId = _tenantId;
    // }

    // public State get_state() {
    //     return _state;
    // }

    // public void set_state(State _state) {
    //     this._state = _state;
    // }

    // public String get_creationTime() {
    //     return _creationTime;
    // }

    // public void set_creationTime(String _creationTime) {
    //     this._creationTime = _creationTime;
    // }

    // public String get_lastModified() {
    //     return _lastModified;
    // }

    // public void set_lastModified(String _lastModified) {
    //     this._lastModified = _lastModified;
    // }


}
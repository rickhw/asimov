package com.gtcafe.asimov.apiserver.platform.task.pojo;

import java.util.Date;

/* part1: write once, read many by user */
/* part2: write once, read many by system, starts with _XXX */
public class TaskMetadata {

	String _apiVersion; // "v1alpha"
	TaskState _state;		// "pending"
	String _creationTime;	// "2021-12-10T00:29:06.800+08:00",
	String _lastModified;	// "2021-12-10T00:29:06.800+08:00"

	public TaskMetadata() {
		// this._resourceId = UUID.randomUUID().toString();
		this._apiVersion = "v1alpha";
		this._state = TaskState.PENDING;
		this._creationTime = new Date().toString();
		this._lastModified = new Date().toString();
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


	public TaskState get_state() {
		return _state;
	}

	public void set_state(TaskState _state) {
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
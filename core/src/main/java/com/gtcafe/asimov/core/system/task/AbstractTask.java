package com.gtcafe.asimov.core.system.task;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gtcafe.asimov.core.utils.TimeUtils;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractTask {

	@Getter @Setter
	private String id;

	@Getter @Setter
	private TaskState state;

	@Getter @Setter
	@JsonProperty("_creationTime")
	String creationTime;	// "2021-12-10T00:29:06.800+08:00",

	@Getter @Setter
	@JsonProperty("_lastModified")
	String lastModified;	// "2021-12-10T00:29:06.800+08:00"

	// private String data;

	public AbstractTask() {
		this.id = UUID.randomUUID().toString();
		this.state = TaskState.PENDING;
		this.creationTime = TimeUtils.timeIso8601(new Date());
		this.lastModified = TimeUtils.timeIso8601(new Date());
	}

}


package com.gtcafe.asimov.apiserver.platform.task;


import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.apiserver.platform.task.pojo.TaskDomainObject;

@Service
public class TaskDataTransferObject {

	// TODO
	public RetrieveTaskResponse convertToTaskResponse(TaskDomainObject tdo) {
		RetrieveTaskResponse obj = new RetrieveTaskResponse();
		obj.setId(tdo.getTaskId());

		return null;
	}

}


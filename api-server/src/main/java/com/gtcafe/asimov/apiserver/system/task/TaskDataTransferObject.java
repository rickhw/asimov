package com.gtcafe.asimov.apiserver.system.task;


import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.system.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;

@Service
public class TaskDataTransferObject {

	// TODO
	public RetrieveTaskResponse convertToTaskResponse(TaskDomainObject tdo) {
		RetrieveTaskResponse obj = new RetrieveTaskResponse();
		obj.setId(tdo.getTaskId());

		return obj;
	}

}


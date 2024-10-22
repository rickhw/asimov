package com.gtcafe.asimov.apiserver.system.task;


import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.system.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.core.platform.hello.SayHelloEventV4;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;

@Service
public class TaskDataTransferObject {

	// TODO
	public RetrieveTaskResponse convertToTaskResponse(TaskDomainObject tdo) {
		RetrieveTaskResponse obj = new RetrieveTaskResponse();
		obj.setId(tdo.getTaskId());
		obj.setState(tdo.getMetadata().get_state().toString());
		obj.setData(tdo.getData());

		return obj;
	}

	public RetrieveTaskResponse convertToTaskV4Response(SayHelloEventV4 tdo) {
		RetrieveTaskResponse obj = new RetrieveTaskResponse();
		obj.setId(tdo.getId());
		obj.setState(tdo.getState().toString());
		obj.setData(tdo.getData());

		return obj;
	}

}


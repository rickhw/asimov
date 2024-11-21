package com.gtcafe.asimov.apiserver.platform.task;


import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.task.operation.RetrieveTaskResponse;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;

@Service
public class TaskMapper {

	public RetrieveTaskResponse convertToTaskV4Response(SayHelloEvent tdo) {
		RetrieveTaskResponse obj = new RetrieveTaskResponse();
		obj.setId(tdo.getId());
		obj.setState(tdo.getState().toString());
		
		obj.setCreationTime(tdo.getCreationTime());
		obj.setLastModified(tdo.getLastModified());

		obj.setData(tdo.getData());

		return obj;
	}

}


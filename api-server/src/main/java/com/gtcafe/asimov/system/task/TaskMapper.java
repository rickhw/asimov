package com.gtcafe.asimov.system.task;


import org.springframework.stereotype.Service;

import com.gtcafe.asimov.rest.domain.task.response.RetrieveTaskResponse;
import com.gtcafe.asimov.system.hello.consumer.HelloEvent;


@Service
public class TaskMapper {

	public RetrieveTaskResponse event2Response(HelloEvent event) {
		RetrieveTaskResponse obj = new RetrieveTaskResponse();
		obj.setId(event.getId());
		obj.setState(event.getState().toString());
		
		obj.setCreationTime(event.getCreationTime());
		// obj.setLastModified(event.getLastModified());

		obj.setData(event.getData());

		return obj;
	}

}


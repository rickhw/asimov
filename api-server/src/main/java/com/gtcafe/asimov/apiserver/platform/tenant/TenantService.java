package com.gtcafe.asimov.apiserver.platform.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.tenant.operation.CreateTenantRequest;
import com.gtcafe.asimov.apiserver.system.MessageProducer;

@Service
public class TenantService {

	@Autowired
	MessageProducer _producer;

	public void registerTenantAsync(CreateTenantRequest request) {
		// 1. create task

		// 2. return task status
	}

	public void deregisterAsync(String id) {

		// Event<DeleteTenantMessage> event = _producer.sendDeregisterEvent(id);
		// IMessage message = (IMessage) event.getData();

		// return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",  event.getEventId(),  message));
	}
}

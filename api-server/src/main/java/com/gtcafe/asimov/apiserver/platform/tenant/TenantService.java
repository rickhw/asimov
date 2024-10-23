package com.gtcafe.asimov.apiserver.platform.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.apiserver.platform.tenant.operation.RegisterTenantRequest;
import com.gtcafe.asimov.apiserver.system.MessageProducer;
import com.gtcafe.asimov.core.cache.CacheRepository;
import com.gtcafe.asimov.core.constants.QueueName;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.platform.tenant.RegisterTenantEvent;
import com.gtcafe.asimov.core.utils.JsonUtils;

@Service
public class TenantService {

	@Autowired
	MessageProducer _producer;

	@Autowired
	CacheRepository _cacheRepos;

	@Autowired
	TenantRepository _repos;

	@Autowired
	private JsonUtils jsonUtils;

	public RegisterTenantEvent registerTenantAsync(RegisterTenantRequest request) {

		// 1. assemble domain object
		RegisterTenantEvent event = new RegisterTenantEvent();
		event.getData().setId(request.getTenantKey());
		
		// 2. sent message to queue, put to dedicated queue
		_producer.sendRegisterTenantEvent(event);

		// 3. store task to cache
		String taskJsonString = jsonUtils.modelToJsonString(event);
		_cacheRepos.saveOrUpdateObject(event.getId(), taskJsonString);

		// 4. persist to database.
		TenantEntity entity = new TenantEntity();
		entity.setTenantKey(request.getTenantKey());
		entity.setDisplayName(request.getDisplayName());
		entity.setDescription(request.getDescription());
		entity.setRootAccount(request.getRootAccount());
		_repos.save(entity);

		return event;
	}

	// public TenantObject retriveTenant(String id) {

	// 	// 2. find the id in cache
	// 	String jsonString = _cacheRepos.retrieveObject(id);
	// 	TenantObject tdo = jsonUtils.jsonStringToModel(jsonString, SayHelloEvent.class);

	// 	// 4. persist to database.
		
		
		
	// 	return event;
	// }

	public void deregisterAsync(String id) {

		// Event<DeleteTenantMessage> event = _producer.sendDeregisterEvent(id);
		// IMessage message = (IMessage) event.getData();

		// return ResponseEntity.ok(String.format("sent, eventId: [%s], message: [%s]",
		// event.getEventId(), message));
	}
}

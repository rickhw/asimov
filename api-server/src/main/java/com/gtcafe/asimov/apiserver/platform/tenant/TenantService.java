package com.gtcafe.asimov.apiserver.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.IMessage;

import java.util.UUID;


@Service
public class TenantService {

	public void createTenant() {
		// 1. create task

		// 2. return task status
	}

}

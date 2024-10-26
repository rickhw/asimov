package com.gtcafe.asimov.core.constants;

// Queue Name, align with application.yml
public final class QueueName {

	// Domains
	public final static String REGISTER_TENANT = "platform.tenant.pending-to-active";
	public final static String DEREGISTER_TENANT = "platform.tenant.active-to-inactive";

	// Platform
	public final static String SAY_HELLO = "platform.sayHello";

	public final static String TASK_QUEUE = "sys.tasks";
}


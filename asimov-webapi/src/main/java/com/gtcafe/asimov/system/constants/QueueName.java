package com.gtcafe.asimov.system.constants;

// Queue Name, align with application.yml
public final class QueueName {

	// Domains
	// public final static String REGISTER_TENANT = "platform.tenant.pending-to-active";
	// public final static String DEREGISTER_TENANT = "platform.tenant.active-to-inactive";

	// Platform
	public final static String HELLO_QUEUE = "asimov.platform.hello";
	public final static String TENANT_QUEUE = "asimov.platform.tenant";
	public final static String TASK_QUEUE = "asimov.platform.task";

	// Exchange
	public final static String FANOUT_EXCHANGE = "asimov.fanoutExchange";
    public final static String DIRECT_EXCHANGE = "asimov.directExchange";

}


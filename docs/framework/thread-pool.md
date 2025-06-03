
## TaskRejectedException



```bash
2025-06-03T21:44:13.065 INFO  [http-nio-0.0.0.0-8080-exec-4] c.g.a.infrastructure.queue.Producer#sendEvent(37) Pushed message to queue: [asimov.platform.hello], exchange: [asimov.directExchange], routingKey: [platform-hello]
2025-06-03T21:44:13.065 WARN  [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-472] o.s.a.r.l.ConditionalRejectingErrorHandler#log(170) Execution of Rabbit message listener failed.
org.springframework.amqp.rabbit.support.ListenerExecutionFailedException: Listener method 'public void com.gtcafe.asimov.system.hello.consumer.HelloConsumer.receiveMessage(java.lang.String,org.springframework.amqp.core.Message,com.rabbitmq.client.Channel,long) throws java.io.IOException' threw exception
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:286)
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandlerAndProcessResult(MessagingMessageListenerAdapter.java:224)
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.onMessage(MessagingMessageListenerAdapter.java:149)
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.doInvokeListener(AbstractMessageListenerContainer.java:1662)
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.actualInvokeListener(AbstractMessageListenerContainer.java:1581)
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.invokeListener(AbstractMessageListenerContainer.java:1569)
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.doExecuteListener(AbstractMessageListenerContainer.java:1560)
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.executeListenerAndHandleException(AbstractMessageListenerContainer.java:1505)
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.lambda$executeListener$8(AbstractMessageListenerContainer.java:1483)
	at io.micrometer.observation.Observation.observe(Observation.java:499)
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.executeListener(AbstractMessageListenerContainer.java:1483)
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer.doReceiveAndExecute(SimpleMessageListenerContainer.java:994)
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer.receiveAndExecute(SimpleMessageListenerContainer.java:941)
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer$AsyncMessageProcessingConsumer.mainLoop(SimpleMessageListenerContainer.java:1325)
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer$AsyncMessageProcessingConsumer.run(SimpleMessageListenerContainer.java:1227)
	at java.base/java.lang.Thread.run(Thread.java:840)
Caused by: org.springframework.core.task.TaskRejectedException: ExecutorService in active state did not accept task: org.springframework.aop.interceptor.AsyncExecutionInterceptor$$Lambda$2459/0x00007a23d0e498e0@585ae259
	at org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor.submit(ThreadPoolTaskExecutor.java:387)
	at org.springframework.aop.interceptor.AsyncExecutionAspectSupport.doSubmit(AsyncExecutionAspectSupport.java:294)
	at org.springframework.aop.interceptor.AsyncExecutionInterceptor.invoke(AsyncExecutionInterceptor.java:129)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:765)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:717)
	at com.gtcafe.asimov.system.hello.consumer.HelloConsumer$$SpringCGLIB$$0.receiveMessage(<generated>)
	at jdk.internal.reflect.GeneratedMethodAccessor26.invoke(Unknown Source)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.springframework.messaging.handler.invocation.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:169)
	at org.springframework.amqp.rabbit.listener.adapter.KotlinAwareInvocableHandlerMethod.doInvoke(KotlinAwareInvocableHandlerMethod.java:45)
	at org.springframework.messaging.handler.invocation.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:119)
	at org.springframework.amqp.rabbit.listener.adapter.HandlerAdapter.invoke(HandlerAdapter.java:75)
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:277)
	... 15 common frames omitted
Caused by: java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@64b7998[Not completed, task = org.springframework.aop.interceptor.AsyncExecutionInterceptor$$Lambda$2459/0x00007a23d0e498e0@585ae259] rejected from org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor$1@292e4068[Running, pool size = 32, active threads = 32, queued tasks = 128, completed tasks = 68158]
	at java.base/java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2065)
	at java.base/java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:833)
	at java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1365)
	at org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor$1.execute(ThreadPoolTaskExecutor.java:269)
	at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:145)
	at org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor.submit(ThreadPoolTaskExecutor.java:384)
	... 29 common frames omitted
2025-06-03T21:44:13.072 INFO  [http-nio-0.0.0.0-8080-exec-3] c.g.a.r.domain.task.TaskController#retrieve(34) taskId: [21211a57-4f02-4f11-bde4-a4fafbf142fc]
2025-06-03T21:44:13.085 INFO  [http-nio-0.0.0.0-8080-exec-7] c.g.a.r.domain.hello.HelloController#sayHelloAsync(76) requestMode: [async]
2025-06-03T21:44:13.086 INFO  [http-nio-0.0.0.0-8080-exec-7] c.g.a.infrastructure.queue.Producer#sendEvent(37) Pushed message to queue: [asimov.platform.hello], exchange: [asimov.directExchange], routingKey: [platform-hello]
2025-06-03T21:44:13.087 INFO  [http-nio-0.0.0.0-8080-exec-10] c.g.a.r.domain.task.TaskController#retrieve(34) taskId: [80698424-2c53-46a9-9e84-76244d84df99]
^C
```
package com.randioo.randioo_server_base.scheduler;

import java.util.concurrent.TimeUnit;

public interface SchedulerInterface {
	public void start();
	public void shutdown(long timeout,TimeUnit unit) throws Exception;
}

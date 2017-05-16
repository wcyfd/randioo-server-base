package com.randioo.randioo_server_base.utils.scheduler;


public interface TimeEvent {

	public int getEndTime();

	public void setEndTime(int endTime);

	public void update(TimeEvent timeEvent);
}

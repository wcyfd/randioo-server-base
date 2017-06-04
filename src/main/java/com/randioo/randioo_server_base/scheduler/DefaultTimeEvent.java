package com.randioo.randioo_server_base.scheduler;

public abstract class DefaultTimeEvent implements TimeEvent {

	private int endTime;

	public abstract void update(TimeEvent timeEvent);

	@Override
	public int getEndTime() {
		return endTime;
	}

	@Override
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

}

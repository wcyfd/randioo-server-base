package com.randioo.randioo_server_base.module.match;

import com.randioo.randioo_server_base.scheduler.TimeEvent;

public abstract class MatchTimeEvent implements TimeEvent {

	private MatchRule matchRule;
	private int endTime;

	public MatchTimeEvent(MatchRule matchRule) {
		this.matchRule = matchRule;
	}

	@Override
	public int getEndTime() {
		return endTime;
	}

	@Override
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	@Override
	public void update(TimeEvent timeEvent) {
		this.outOfTime(matchRule);
	}

	public abstract void outOfTime(MatchRule matchRule);

}

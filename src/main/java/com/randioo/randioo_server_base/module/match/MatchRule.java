package com.randioo.randioo_server_base.module.match;

public class MatchRule {
	enum MatchState {
		MATCH_READY, MATCH_CANCEL, MATCH_SUCCESS
	}

	private int waitTime;
	private String id;
	private MatchState state;

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MatchState getState() {
		return state;
	}

	protected void setState(MatchState state) {
		this.state = state;
	}

}

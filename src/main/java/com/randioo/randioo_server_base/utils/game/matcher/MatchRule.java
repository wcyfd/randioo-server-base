package com.randioo.randioo_server_base.utils.game.matcher;

import java.util.concurrent.TimeUnit;

public class MatchRule {
	private Matchable matchTarget;
	private int playerCount;
	private boolean matchNPC;
	private long waitTime;
	private TimeUnit waitUnit;

	public Matchable getMatchTarget() {
		return matchTarget;
	}

	public void setMatchTarget(Matchable matchTarget) {
		this.matchTarget = matchTarget;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public boolean isMatchNPC() {
		return matchNPC;
	}

	public void setMatchNPC(boolean matchNPC) {
		this.matchNPC = matchNPC;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public TimeUnit getWaitUnit() {
		return waitUnit;
	}

	public void setWaitUnit(TimeUnit waitUnit) {
		this.waitUnit = waitUnit;
	}

}

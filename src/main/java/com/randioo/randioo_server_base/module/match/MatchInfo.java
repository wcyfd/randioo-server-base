package com.randioo.randioo_server_base.module.match;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class MatchInfo {
	private int matchId;
	private boolean matchComplete;
	private boolean matchCancel;
	private ScheduledFuture<?> scheduleFuture;
	private MatchRule matchRule;
	private List<Matchable> matchables = new ArrayList<>();

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public ScheduledFuture<?> getScheduleFuture() {
		return scheduleFuture;
	}

	public void setScheduleFuture(ScheduledFuture<?> scheduleFuture) {
		this.scheduleFuture = scheduleFuture;
	}

	public boolean isMatchCancel() {
		return matchCancel;
	}

	public void setMatchCancel(boolean matchCancel) {
		this.matchCancel = matchCancel;
	}

	public boolean isMatchComplete() {
		return matchComplete;
	}

	public void setMatchComplete(boolean matchComplete) {
		this.matchComplete = matchComplete;
	}

	public MatchRule getMatchRule() {
		return matchRule;
	}

	public void setMatchRule(MatchRule matchRule) {
		this.matchRule = matchRule;
	}

	public List<Matchable> getMatchables() {
		return matchables;
	}
}

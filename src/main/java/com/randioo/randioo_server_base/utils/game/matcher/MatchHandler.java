package com.randioo.randioo_server_base.utils.game.matcher;

public interface MatchHandler {
	public void matchSuccess(MatchInfo matchInfo, Matchable role);

	public void matchComplete(MatchInfo matchInfo);
	
	public boolean needWaitMatch(MatchInfo matchInfo);

	public boolean matchRule(MatchRule myMatchRule,MatchInfo otherMatchInfo);

	public Matchable getAutoMatchRole(MatchInfo matchInfo);

	public void destroyMatchInfo(MatchInfo matchInfo);
}

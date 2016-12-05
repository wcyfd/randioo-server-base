package com.randioo.randioo_server_base.utils.game.matcher;

public interface MatchHandler {
	
	public MatchInfo createMatchInfo(MatchRule matchRule);
	
	public void matchSuccess(MatchInfo matchInfo, MatchRule matchRule);

	public void matchComplete(MatchInfo matchInfo);
	
	public boolean needWaitMatch(MatchInfo matchInfo);

	public boolean matchRule(MatchRule myMatchRule,MatchInfo otherMatchInfo);

	public MatchRule getAutoMatchRole(MatchInfo matchInfo);

	public void destroyMatchInfo(MatchInfo matchInfo);

	public void cancelMatch(Matchable matchable);
}

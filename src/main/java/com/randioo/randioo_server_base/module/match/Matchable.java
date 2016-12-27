package com.randioo.randioo_server_base.module.match;

public interface Matchable {
	public void setMatchInfo(MatchInfo matchInfo);

	public MatchInfo getMatchInfo();
	
	public void setNpc(boolean isNpc);
	
	public boolean isNPC();
}
package com.randioo.randioo_server_base.utils.game.matcher;

public interface Matchable {
	public void setMatchInfo(MatchInfo matchInfo);

	public MatchInfo getMatchInfo();
	
	public void setNpc(boolean isNpc);
	
	public boolean isNPC();
}
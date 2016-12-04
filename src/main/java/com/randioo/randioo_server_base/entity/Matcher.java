package com.randioo.randioo_server_base.entity;

import com.randioo.randioo_server_base.utils.game.matcher.MatchInfo;
import com.randioo.randioo_server_base.utils.game.matcher.Matchable;

public class Matcher implements Matchable{

	private MatchInfo matchInfo;
	private boolean prepare;
	private boolean npc;
	@Override
	public void setMatchInfo(MatchInfo matchInfo) {
		// TODO Auto-generated method stub
		this.matchInfo = matchInfo;
	}

	@Override
	public MatchInfo getMatchInfo() {
		// TODO Auto-generated method stub
		return matchInfo;
	}

	@Override
	public void setPrepare(boolean prepare) {
		// TODO Auto-generated method stub
		this.prepare = prepare;
	}

	@Override
	public boolean isPrepare() {
		// TODO Auto-generated method stub
		return prepare;
	}

	@Override
	public void setNpc(boolean isNpc) {
		// TODO Auto-generated method stub
		this.npc = isNpc;
	}

	@Override
	public boolean isNPC() {
		// TODO Auto-generated method stub
		return npc;
	}

}

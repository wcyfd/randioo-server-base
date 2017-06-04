package com.randioo.randioo_server_base.module.match;

import java.util.Map;

public interface MatchHandler {
	public void outOfTime(MatchRule matchRule);

	public boolean checkMatchRule(MatchRule rule1, MatchRule rule2);

	public boolean checkArriveMaxCount(MatchRule rule, Map<String, MatchRule> matchRuleMap);

	public void matchSuccess(Map<String, MatchRule> matchRule);
}

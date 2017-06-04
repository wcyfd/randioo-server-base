package com.randioo.randioo_server_base.module.match;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class MatchRuleCache {
	private Map<String, MatchRule> matchRuleMap = new ConcurrentHashMap<>();

	public Map<String, MatchRule> getMatchRuleMap() {
		return matchRuleMap;
	}

	public Set<String> cancelMatchRuleIdSet = new HashSet<>();

	public Set<String> getDeleteMatchRuleIdSet() {
		System.out.println("DeleteMatchRuleIdSet" + cancelMatchRuleIdSet);
		return cancelMatchRuleIdSet;
	}

	public Map<String, MatchRule> matchTempMap = new HashMap<>();

	public Map<String, MatchRule> getMatchTempMap() {
		return matchTempMap;
	}

	public Set<Lock> locksTempMap = new HashSet<>();

	public Set<Lock> getLocksTempMap() {
		return locksTempMap;
	}

	public Set<String> needDeleteIdTempSet = new HashSet<>();

	public Set<String> getNeedDeleteIdTempSet() {
		return needDeleteIdTempSet;
	}
}

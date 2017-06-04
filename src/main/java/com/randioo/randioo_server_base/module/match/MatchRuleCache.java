package com.randioo.randioo_server_base.module.match;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class MatchRuleCache {
	private static Map<String, MatchRule> matchRuleMap = new ConcurrentHashMap<>();

	public static Map<String, MatchRule> getMatchRuleMap() {
		return matchRuleMap;
	}

	public static Set<String> cancelMatchRuleIdSet = new HashSet<>();

	public static Set<String> getDeleteMatchRuleIdSet() {
		System.out.println("DeleteMatchRuleIdSet"+cancelMatchRuleIdSet);
		return cancelMatchRuleIdSet;
	}

	public static Map<String, MatchRule> matchTempMap = new HashMap<>();

	public static Map<String, MatchRule> getMatchTempMap() {
		return matchTempMap;
	}

	public static Set<Lock> locksTempMap = new HashSet<>();

	public static Set<Lock> getLocksTempMap() {
		return locksTempMap;
	}

	public static Set<String> needDeleteIdTempSet = new HashSet<>();

	public static Set<String> getNeedDeleteIdTempSet() {
		return needDeleteIdTempSet;
	}
}

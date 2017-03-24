package com.randioo.randioo_server_base.module.friend;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.util.ConcurrentHashSet;

public class FriendCache {

	// 关系表
	private static ConcurrentHashMap<String, Relation> relationMap = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<Integer, Set<String>> relationKeyMap = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, Relation> getRelationMap() {
		return relationMap;
	}

	public static Set<String> getRelationKeyByRoleId(int roleId) {
		Set<String> set = relationKeyMap.get(roleId);
		if (set == null) {
			set = new ConcurrentHashSet<>();
			relationKeyMap.putIfAbsent(roleId, set);
		}

		return relationKeyMap.get(roleId);
	}

	public static void addRelationKeyMap(RelationKey relationKey) {
		getRelationKeyByRoleId(relationKey.getRoleId1()).add(relationKey.getKey());
		getRelationKeyByRoleId(relationKey.getRoleId2()).add(relationKey.getKey());
	}

}

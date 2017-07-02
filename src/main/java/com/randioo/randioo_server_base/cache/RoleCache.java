package com.randioo.randioo_server_base.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.entity.RoleInterface;

public class RoleCache {
	/** 姓名集合 */
	private static Map<String, Boolean> nameSet = new ConcurrentHashMap<>();
	/** 帐号集合 */
	private static Map<String, Boolean> accountSet = new ConcurrentHashMap<>();

	private static Map<Integer, String> roleIdAccountMap = new ConcurrentHashMap<>();

	public static Map<String, Boolean> getNameSet() {
		return nameSet;
	}

	public static Map<String, Boolean> getAccountSet() {
		return accountSet;
	}

	public static Map<Integer, String> getRoleIdAccountMap() {
		return roleIdAccountMap;
	}

}

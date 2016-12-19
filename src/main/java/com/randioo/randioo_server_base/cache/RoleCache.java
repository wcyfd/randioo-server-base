package com.randioo.randioo_server_base.cache;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.entity.RoleInterface;

public class RoleCache {
	/** 所有玩家缓存信息 */
	private static ConcurrentMap<Integer, RoleInterface> roleMap = new ConcurrentHashMap<>();
	private static ConcurrentMap<String, RoleInterface> roleAccountMap = new ConcurrentHashMap<>();
	/** 姓名集合 */
	private static Set<String> nameSet = new HashSet<>();
	/** 登录账号集合 */
	private static Set<String> accountSet = new HashSet<>();

	/**
	 * 根据id获取角色缓存
	 * 
	 * @param id
	 * @return
	 */
	public static RoleInterface getRoleById(int id) {
		return getRoleMap().get(id);
	}

	public static RoleInterface getRoleBySession(IoSession ioSession) {
		try {
			Integer roleId = (Integer) ioSession.getAttribute("roleId");
			if (roleId == null)
				return null;
			RoleInterface role = getRoleMap().get(roleId);
			if (role == null)
				return null;
			return role;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 更具账号获取角色缓存
	 * 
	 * @param name
	 * @return
	 */
	public static RoleInterface getRoleByAccount(String account) {
		return roleAccountMap.get(account);
	}

	public static Set<String> getNameSet() {
		return nameSet;
	}

	public static Set<String> getAccountSet() {
		return accountSet;
	}

	public static ConcurrentMap<Integer, RoleInterface> getRoleMap() {
		return roleMap;
	}

	public static synchronized void putNewRole(RoleInterface role) {
		accountSet.add(role.getAccount());
		nameSet.add(role.getName());
		roleMap.put(role.getRoleId(), role);
		roleAccountMap.put(role.getAccount(), role);
	}

	public static void putRoleCache(RoleInterface role) {
		roleMap.put(role.getRoleId(), role);
		roleAccountMap.put(role.getAccount(), role);
	}

	public static void serverInit(RoleInterface role) {
		accountSet.add(role.getAccount());
		nameSet.add(role.getName());
	}

	public static ConcurrentMap<String, RoleInterface> getRoleAccountMap() {
		return roleAccountMap;
	}

}
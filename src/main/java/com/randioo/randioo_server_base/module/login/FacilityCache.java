package com.randioo.randioo_server_base.module.login;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家设备缓存
 * 
 * @author wcy 2017年7月1日
 *
 */
public class FacilityCache {

	private static Map<Integer, RoleFacilities> roleFacilitiesMap = new ConcurrentHashMap<>();

	public static Map<Integer, RoleFacilities> getRoleFacilitiesMap() {
		return roleFacilitiesMap;
	}

	private static Map<Integer, Facility> loginFacilityMap = new ConcurrentHashMap<>();

	public static Map<Integer, Facility> getLoginFacilityMap() {
		return loginFacilityMap;
	}
}

package com.randioo.randioo_server_base.module.login;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户设备
 * 
 * @author wcy 2017年7月1日
 *
 */
public class RoleFacilities {
	/** 帐号 */
	private int roleId;
	/** mac,device */
	private Map<String, Facility> facilities = new HashMap<>();

	public Map<String, Facility> getFacilities() {
		return facilities;
	}

	public int getRoleId() {
		return roleId;
	}

	protected void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}

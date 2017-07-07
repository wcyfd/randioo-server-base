package com.randioo.randioo_server_base.module.login;

import org.apache.mina.core.session.IoSession;

/**
 * 设备
 * 
 * @author wcy 2017年7月1日
 *
 */
public class Facility {
	/** 物理地址 */
	private String macAddress;
	/** 玩家id */
	private int roleId;
	/** 会话连接 */
	private IoSession session;

	public IoSession getSession() {
		return session;
	}

	protected void setSession(IoSession session) {
		this.session = session;
	}

	public String getMacAddress() {
		return macAddress;
	}

	protected void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public int getRoleId() {
		return roleId;
	}

	protected void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}

package com.randioo.randioo_server_base.entity;

public interface RoleInterface {

	public void setRoleId(int roleId);

	public int getRoleId();

	public String getAccount();

	public void setAccount(String account);

	public String getName();

	public void setName(String name);

	public void setLoginTime(int loginTime);

	public int getLoginTime();

	public void setOfflineTime(int offlineTime);

	public int getOfflineTime();

	public void setCreateTime(int createTime);

	public int getCreateTime();
}

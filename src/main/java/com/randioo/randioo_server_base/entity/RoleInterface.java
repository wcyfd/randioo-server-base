package com.randioo.randioo_server_base.entity;

public interface RoleInterface {

	public void setRoleId(int roleId);

	public int getRoleId();

	public String getAccount();

	public void setAccount(String account);

	public String getName();

	public void setName(String name);

	public void setLoginTimeStr(String loginTimeStr);

	public String getLoginTimeStr();

	public void setOfflineTimeStr(String offlineTimeStr);

	public String getOfflineTimeStr();

	public void setCreateTimeStr(String createTimeStr);

	public String getCreateTimeStr();
	
	public void setLoadTimeStr(String loadTimeStr);
	
	public String getLoadTimeStr();
}

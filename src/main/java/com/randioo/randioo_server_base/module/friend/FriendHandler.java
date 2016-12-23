package com.randioo.randioo_server_base.module.friend;

import com.randioo.randioo_server_base.entity.RoleInterface;

public interface FriendHandler {

	/**
	 * 重复添加好友
	 * 
	 * @param roleInterface
	 * @param targetAccount
	 * @author wcy 2016年12月22日
	 */
	void repeatedAddFriend(RoleInterface roleInterface, String targetAccount);

	/**
	 * 发送申请
	 * 
	 * @param roleInterface
	 * @param targetAccount
	 * @author wcy 2016年12月22日
	 */
	void sendApply(RoleInterface roleInterface, String targetAccount);

	/**
	 * 
	 * @param targetAccount
	 * @return
	 * @author wcy 2016年12月22日
	 */
	Friendable getFriendableByAccount(String targetAccount);

	RoleInterface getRoleInterfaceByAccount(String account);

	/**
	 * 同意添加好友
	 * 
	 * @param roleInterface
	 * @param targetRoleInterface
	 * @author wcy 2016年12月22日
	 */
	void agreeAddFriend(RoleInterface roleInterface, RoleInterface targetRoleInterface);

}

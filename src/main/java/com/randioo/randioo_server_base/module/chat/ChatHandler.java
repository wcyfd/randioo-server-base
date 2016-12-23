package com.randioo.randioo_server_base.module.chat;

import java.util.List;
import java.util.Set;

import com.randioo.randioo_server_base.entity.RoleInterface;

public interface ChatHandler {

	RoleInterface getRoleInterfaceByAccount(String account);

	Chatable getChatableByAccount(String account);

	Object getChatInfo(RoleInterface role, String context, int sendType);

	Object parseToProtocolMessage(String account, Object chatInfo);

	Set<String> getNeedSendAccountSet(RoleInterface role, String roomId, int sendType);

	void showUnreadChatByAccount(RoleInterface roleInterface, String account, List<Object> objList);

}

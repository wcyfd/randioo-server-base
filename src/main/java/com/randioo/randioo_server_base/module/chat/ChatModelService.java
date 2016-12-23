package com.randioo.randioo_server_base.module.chat;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface ChatModelService extends BaseServiceInterface {

	public void setChatHandler(ChatHandler handler);

	/**
	 * 发送聊天信息
	 * 
	 * @param context
	 * @return
	 * @author wcy 2016年12月19日
	 */
	public void sendChat(RoleInterface role, Chatable chatable, String roomId, String context, int sendType);

	public void showChatByAccount(RoleInterface roleInterface, Chatable chatable, String account);
}

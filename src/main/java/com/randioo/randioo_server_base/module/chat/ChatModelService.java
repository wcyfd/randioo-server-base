package com.randioo.randioo_server_base.module.chat;

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
	public void sendPrivateChat(Chatable chatable, int targetChatableKey, String context);

	public void sendPublicChat(Chatable chatable, String context);

	void joinPublic(Chatable chatable);

	void exitPublic(Chatable chatable);
}

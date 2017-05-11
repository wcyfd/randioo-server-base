package com.randioo.randioo_server_base.module.chat;


public interface ChatHandler {

	public ChatInfo createChatInfo(Chatable chatable, String context);

	public void noticePrivateChatInfo(ChatInfo chatInfo, int chatable);

	public void noticePublicChatInfo(ChatInfo chatInfo, int receiveChatable);

}

package com.randioo.randioo_server_base.module.chat;

import java.util.Queue;

import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.utils.TimeUtils;

@Service("chatModelService")
public class ChatModelServiceImpl extends BaseService implements ChatModelService {

	private ChatHandler chatHandler;

	@Override
	public void setChatHandler(ChatHandler handler) {
		this.chatHandler = handler;
	}

	@Override
	public void sendPrivateChat(Chatable chatable, int targetChatableKey, String context) {
		int myKey = chatable.getChatKey();
		int otherKey = targetChatableKey;

		int nowTime = TimeUtils.getNowTime();
		// 创建聊天信息
		ChatInfo chatInfo = chatHandler.createChatInfo(chatable, context);
		chatInfo.setKey(chatable.getChatKey());
		chatInfo.setTargetKey(targetChatableKey);		
		chatInfo.setTime(nowTime);
		chatInfo.setTxt(context);

		//加入到对方消息队列中
		Queue<ChatInfo> otherQueue = ChatInfoCache.getSession(otherKey, myKey);

		this.synSendQueue2Client(otherQueue, chatInfo, otherKey);
	}

	/**
	 * 发送到客户端
	 * 
	 * @param queue
	 * @param newChatInfo
	 * @param chatable
	 * @author wcy 2017年2月15日
	 */
	private void synSendQueue2Client(Queue<ChatInfo> queue, ChatInfo newChatInfo, int chatableKey) {
		synchronized (queue) {
			queue.add(newChatInfo);
			chatHandler.noticePrivateChatInfo(newChatInfo, chatableKey);
		}
	}

	@Override
	public void sendPublicChat(Chatable chatable, String context) {
		int nowTime = TimeUtils.getNowTime();
		ChatInfo chatInfo = chatHandler.createChatInfo(chatable, context);
		chatInfo.setKey(chatable.getChatKey());
		chatInfo.setTime(nowTime);
		chatInfo.setTxt(context);

		for (Integer chatableInPublic : ChatInfoCache.getPublicMap().values())
			chatHandler.noticePublicChatInfo(chatInfo, chatableInPublic);
	}

	@Override
	public void joinPublic(Chatable chatable) {
		ChatInfoCache.getPublicMap().putIfAbsent(chatable.getChatKey(), chatable.getChatKey());
	}

	@Override
	public void exitPublic(Chatable chatable) {
		ChatInfoCache.getPublicMap().remove(chatable.getChatKey());
	}

}

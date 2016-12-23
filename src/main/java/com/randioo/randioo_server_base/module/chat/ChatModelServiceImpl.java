package com.randioo.randioo_server_base.module.chat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.cache.SessionCache;
import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseService;

public class ChatModelServiceImpl extends BaseService implements ChatModelService {

	private ChatHandler chatHandler;

	@Override
	public void setChatHandler(ChatHandler chatHandler) {
		this.chatHandler = chatHandler;
	}

	@Override
	public void sendChat(RoleInterface role, Chatable chatable, String targetAccount, String context, int sendType) {
		Set<String> accountSet = chatHandler.getNeedSendAccountSet(role, targetAccount, sendType);
		if (accountSet == null) {
			return;
		}

		Object chatInfo = chatHandler.getChatInfo(role, context, sendType);
		Object sendMessage = chatHandler.parseToProtocolMessage(targetAccount, chatInfo);

		for (String account : accountSet) {
			RoleInterface roleInterface = chatHandler.getRoleInterfaceByAccount(account);
			Chatable targetChatable = chatHandler.getChatableByAccount(account);

			IoSession targetSession = SessionCache.getSessionById(roleInterface.getRoleId());
			if (targetSession == null) {
				this.addUnreadChatInfoCount(targetChatable, account);
			}
			this.getHistoryChatInfoList(targetChatable, account).add(chatInfo);
			targetSession.write(sendMessage);
		}

	}

	@Override
	public void showChatByAccount(RoleInterface roleInterface, Chatable chatable, String account) {
		List<Object> historyList = this.getHistoryChatInfoList(chatable, account);
		int count = this.getUnreadChatInfoList(chatable, account);
		int historyListLen = historyList.size();
		List<Object> objList = new ArrayList<>(historyListLen);
		for (int i = 1; i <= count; i++) {
			Object obj = historyList.get(historyListLen - i);
			objList.add(obj);
		}

		chatable.getUnreadChatCountMap().put(account, 0);
		chatHandler.showUnreadChatByAccount(roleInterface, account, objList);

	}

	/**
	 * 历史聊天记录
	 * 
	 * @param chatable
	 * @param account
	 * @return
	 * @author wcy 2016年12月21日
	 */
	private List<Object> getHistoryChatInfoList(Chatable chatable, String account) {
		List<Object> targetHistoryChatList = chatable.getHistoryChatMap().get(account);
		if (targetHistoryChatList == null) {
			targetHistoryChatList = new LinkedList<>();
			chatable.getHistoryChatMap().put(account, targetHistoryChatList);
		}
		return targetHistoryChatList;
	}

	/**
	 * 未读聊天记录
	 * 
	 * @param chatable
	 * @param account
	 * @return
	 * @author wcy 2016年12月21日
	 */
	private int getUnreadChatInfoList(Chatable chatable, String account) {
		Integer count = chatable.getUnreadChatCountMap().get(account);
		return count != null ? count : 0;
	}

	private void addUnreadChatInfoCount(Chatable chatable, String account) {
		int count = this.getUnreadChatInfoList(chatable, account);
		chatable.getUnreadChatCountMap().put(account, count + 1);
	}

}

package com.randioo.randioo_server_base.module.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class ChatInfoCache {
	private static Map<Integer, Map<Integer, Queue<ChatInfo>>> unreadMap = new HashMap<>();// 私人频道
	private static ConcurrentHashMap<Integer, Chatable> publicMap = new ConcurrentHashMap<>();// 公共频道

	public static void initChatInfo(List<ChatInfo> chatInfoList) {
		for (ChatInfo chatInfo : chatInfoList) {
			int receiveId = chatInfo.getTargetKey();
			int sendId = chatInfo.getKey();
			getContact(receiveId).get(sendId).add(chatInfo);
		}
	}

	public static Queue<ChatInfo> getSession(int myChatableKey, int targetChatableKey) {
		int otherKey = targetChatableKey;
		int myKey = myChatableKey;

		if (checkSessionAllExist(myKey, otherKey))
			return unreadMap.get(myKey).get(otherKey);

		synchronized (unreadMap) {
			if (checkSessionAllExist(myKey, otherKey))
				return unreadMap.get(myKey).get(otherKey);

			createSession(myKey, otherKey);
			createSession(otherKey, myKey);
		}

		return unreadMap.get(myKey).get(otherKey);
	}

	private static void createSession(int myKey, int otherKey) {
		createContact(myKey);
		Map<Integer, Queue<ChatInfo>> myChatInfoMap = unreadMap.get(myKey);

		Queue<ChatInfo> chatQueue = myChatInfoMap.get(otherKey);
		if (chatQueue == null) {
			chatQueue = new PriorityBlockingQueue<>();
			myChatInfoMap.put(otherKey, chatQueue);
		}
	}

	public static Map<Integer, Queue<ChatInfo>> getContact(int myKey) {
		if (checkContactExist(myKey)) {
			return unreadMap.get(myKey);
		}
		synchronized (unreadMap) {
			if (checkContactExist(myKey)) {
				return unreadMap.get(myKey);
			}
			createContact(myKey);
		}
		return unreadMap.get(myKey);
	}

	private static void createContact(int myKey) {
		Map<Integer, Queue<ChatInfo>> myChatInfoMap = unreadMap.get(myKey);
		if (myChatInfoMap == null) {
			myChatInfoMap = new ConcurrentHashMap<Integer, Queue<ChatInfo>>();
			unreadMap.put(myKey, myChatInfoMap);
		}
	}

	private static boolean checkContactExist(int myKey) {
		if (unreadMap.get(myKey) == null) {
			return false;
		}
		return true;
	}

	private static boolean checkSessionAllExist(int myKey, int otherKey) {
		return checkSessionExist(myKey, otherKey) && checkSessionExist(otherKey, myKey);
	}

	private static boolean checkSessionExist(int myKey, int otherKey) {
		Map<Integer, Queue<ChatInfo>> myAllSession = unreadMap.get(myKey);
		if (myAllSession == null) {
			return false;
		}
		Queue<ChatInfo> queue = myAllSession.get(otherKey);
		if (queue == null) {
			return false;
		}
		return true;

	}

	public static ConcurrentHashMap<Integer, Chatable> getPublicMap() {
		return publicMap;
	}

	public static Map<Integer, Map<Integer, Queue<ChatInfo>>> getPrivateMap() {
		return unreadMap;
	}
}

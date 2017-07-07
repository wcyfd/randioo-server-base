package com.randioo.randioo_server_base.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

public class SessionCache {
	private static ConcurrentMap<Integer, IoSession> sessionMap = new ConcurrentHashMap<Integer, IoSession>();

	public static IoSession getSessionById(int id) {
		return sessionMap.get(id);
	}

	public static void addSession(int id, IoSession session) {
		sessionMap.put(id, session);
	}

	/**
	 * 获取所有session
	 * 
	 * @return
	 */
	public static Collection<IoSession> getAllSession() {
		return sessionMap.values();
	}

}

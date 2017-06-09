package com.randioo.randioo_server_base.utils;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.cache.SessionCache;

public class SessionUtils {
	public static void sc(IoSession session, Object message) {
		if (session != null) {
			session.write(message);
		}
	}

	public static void sc(int roleId, Object message) {
		IoSession session = SessionCache.getSessionById(roleId);
		sc(session, message);
	}
	
}

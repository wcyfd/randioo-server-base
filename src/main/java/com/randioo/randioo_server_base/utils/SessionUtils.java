package com.randioo.randioo_server_base.utils;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.randioo.randioo_server_base.cache.SessionCache;

public class SessionUtils {
	private static Logger logger = LoggerFactory.getLogger(SessionUtils.class.getSimpleName());

	public static void sc(IoSession session, Object message) {
		if (session != null) {
			session.write(message);
		}
	}

	public static void sc(int roleId, Object message) {
		logger.info("roleId=" + roleId + " " + message);
		IoSession session = SessionCache.getSessionById(roleId);
		sc(session, message);
	}

}

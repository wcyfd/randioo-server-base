package com.randioo.randioo_server_base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.randioo.randioo_server_base.cache.SessionCache;
import com.randioo.randioo_server_base.template.Session;

@Component
public class SessionUtils {
    private static Logger logger = LoggerFactory.getLogger(SessionUtils.class.getSimpleName());

    public static void sc(Object session, Object message) {
        Session.write(session, message);
    }

    public static void sc(int roleId, Object message) {
        logger.info("roleId=" + roleId + " " + message);
        Object session = SessionCache.getSessionById(roleId);
        sc(session, message);
    }

}

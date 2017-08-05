package com.randioo.randioo_server_base.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

/**
 * 用户连接缓存
 * 
 * @author wcy 2017年8月5日
 *
 */
public class SessionCache {
    /** 连接表，玩家id与连接绑定 */
    private static ConcurrentMap<Integer, IoSession> sessionMap = new ConcurrentHashMap<Integer, IoSession>();

    /**
     * 通过玩家id获得连接
     * 
     * @param id 玩家id
     * @return IoSession
     * @author wcy 2017年8月5日
     */
    public static IoSession getSessionById(int id) {
        return sessionMap.get(id);
    }

    /**
     * 添加新的连接,该方法不需要自己调用,由登录流程完成
     * 
     * @param id
     * @param session
     * @author wcy 2017年8月5日
     */
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

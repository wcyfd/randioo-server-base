package com.randioo.randioo_server_base.template;

import java.util.ArrayList;
import java.util.List;

public class Session {

    protected static List<ISession> isessions;

    protected static List<SendCompleteCallback> handlerChains = new ArrayList<>();

    public static void addISession(List<ISession> session) {
        isessions = session;
    }

    /**
     * 获得通用接口
     * 
     * @param session
     * @return
     * @author wcy 2017年9月7日
     */
    private static ISession getISession(Object session) {
        if (session == null) {
            return null;
        }
        int size = isessions.size();

        ISession isession = null;
        for (int i = 0; i < size; i++) {
            ISession temp = isessions.get(i);
            if (temp.isInstanceof(session)) {
                isession = temp;
                break;
            }
        }
        return isession;
    }

    public static <T> T getAttribute(Object session, Object key) {
        ISession isession = getISession(session);
        if (isession == null) {
            return null;
        }
        Object obj = isession.getAttribute(session, key);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    public static void setAttribute(Object session, Object key, Object value) {
        ISession isession = getISession(session);
        if (isession == null) {
            return;
        }
        if (value == null) {
            return;
        }
        isession.setAttribute(session, key, value);
    }

    public static void write(Object session, Object value) {
        if (value == null) {
            return;
        }
        ISession isession = getISession(session);
        if (isession == null) {
            return;
        }
        isession.write(session, value);
        sendComplete(session, value);
    }

    public static void close(Object session) {
        ISession isession = getISession(session);
        if (isession == null) {
            return;
        }
        isession.close(session);
    }

    public static void addHandler(SendCompleteCallback function) {
        handlerChains.add(function);
    }

    private static void sendComplete(Object session, Object value) {
        for (int i = 0; i < handlerChains.size(); i++) {
            SendCompleteCallback function = handlerChains.get(i);
            function.callback(session, value);
        }
    }

    public static boolean isConnected(Object session) {
        ISession isession = getISession(session);
        if (isession == null) {
            return false;
        }
        return isession.isConnected(session);
    }

    /**
     * 发送成功的回调函数
     * 
     * @author wcy 2017年9月15日
     *
     */
    public interface SendCompleteCallback {
        public void callback(Object session, Object value);
    }
}

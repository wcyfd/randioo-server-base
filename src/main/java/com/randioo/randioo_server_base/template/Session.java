package com.randioo.randioo_server_base.template;

import java.util.ArrayList;
import java.util.List;

import com.randioo.randioo_server_base.utils.PackageUtil;
import com.randioo.randioo_server_base.utils.SpringContext;

public class Session {

    protected static List<ISession> isessions = null;

    public static void initISession() {
        initISession("com.randioo.randioo_server_base.session");
    }

    protected static List<SendCompleteCallback> handlerChains = new ArrayList<>();

    /**
     * 初始化所有的通信接口
     * 
     * @param packageName
     * @author wcy 2017年9月7日
     */
    public static void initISession(String packageName) {
        List<Class<?>> clazzes = PackageUtil.getClasses(packageName);
        isessions = new ArrayList<ISession>(clazzes.size());

        for (Class<?> clazz : clazzes) {
            Class<?>[] Interfaces = clazz.getInterfaces();
            for (Class<?> Interface : Interfaces) {
                if (ISession.class.equals(Interface)) {
                    ISession obj = SpringContext.getBean(clazz);
                    isessions.add(obj);
                    break;
                }
            }
        }
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

    public static Object getAttribute(Object session, Object key) {
        ISession isession = getISession(session);
        if (isession == null) {
            return null;
        }
        return isession.getAttribute(session, key);
    }

    public static void setAttribute(Object session, Object key, Object value) {
        ISession isession = getISession(session);
        if (isession == null) {
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

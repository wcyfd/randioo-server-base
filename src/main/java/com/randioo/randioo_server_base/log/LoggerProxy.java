package com.randioo.randioo_server_base.log;

import java.lang.reflect.Proxy;

import org.slf4j.Logger;

/**
 * 日志代理
 * 
 * @author wcy 2017年10月4日
 *
 */
public class LoggerProxy {

    /**
     * 代理
     * 
     * @param logger
     * @param name
     * @return
     * @author wcy 2017年10月4日
     */
    public static Logger proxyByName(Logger logger, String name) {
        LogInvocationHandler handler = new LogInvocationHandler(logger, name);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Object obj = Proxy.newProxyInstance(loader, new Class[] { Logger.class }, handler);
        Logger proxyLogger = (Logger) obj;
        return proxyLogger;
    }
}

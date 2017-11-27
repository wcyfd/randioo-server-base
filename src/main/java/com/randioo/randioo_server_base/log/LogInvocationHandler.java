package com.randioo.randioo_server_base.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;

public class LogInvocationHandler implements InvocationHandler {
    private Logger logger;
    private String name;

    public LogInvocationHandler(Logger proxyLogger, String name) {
        this.logger = proxyLogger;
        this.name = name;
    }

    @Override
    public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
        int index = getStringParameterIndex(arg1);
        if (index != -1) {
            Object value = arg2[index];
            arg2[index] = name + (value == null ? "" : value);
        }

        return arg1.invoke(logger, arg2);
    }

    private int getStringParameterIndex(Method method) {
        Class<?>[] params = method.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals(String.class)) {
                return i;
            }
        }
        return -1;
    }
}

package com.randioo.randioo_server_base.template;

public interface ISession {
    boolean isInstanceof(Object obj);

    Object getAttribute(Object session, Object key);

    void setAttribute(Object session, Object key, Object value);

    void write(Object session, Object value);

    void close(Object session);

    boolean isConnected(Object session);
}

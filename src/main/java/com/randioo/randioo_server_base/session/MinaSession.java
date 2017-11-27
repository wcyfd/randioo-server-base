package com.randioo.randioo_server_base.session;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.template.ISession;

public class MinaSession implements ISession {

    @Override
    public Object getAttribute(Object session, Object key) {
        return ((IoSession) session).getAttribute(key);
    }

    @Override
    public void setAttribute(Object session, Object key, Object value) {
        ((IoSession) session).setAttribute(key, value);
    }

    @Override
    public void write(Object session, Object obj) {
        ((IoSession) session).write(obj);
    }

    @Override
    public void close(Object session) {
        ((IoSession) session).close(true);
    }

    @Override
    public boolean isInstanceof(Object obj) {
        return obj instanceof IoSession;
    }

    @Override
    public boolean isConnected(Object session) {
        return ((IoSession) session).isConnected();
    }

}

package com.randioo.randioo_server_base.eventbus;

public interface Listener<T extends Event> {
    public void listen(T event);
}

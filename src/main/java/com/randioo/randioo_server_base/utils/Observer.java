package com.randioo.randioo_server_base.utils;

public interface Observer {
	public void update(Observer observer, String msg, Object... args);
}

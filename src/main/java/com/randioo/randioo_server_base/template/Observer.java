package com.randioo.randioo_server_base.template;

public interface Observer {
	public void update(Observer observer, String msg, Object... args);
}

package com.randioo.randioo_server_base.utils.template;

public interface Parser<T,V> {
	public T parse(V param);
}

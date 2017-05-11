package com.randioo.randioo_server_base.db;

import java.util.concurrent.Callable;

public abstract class DBCallable<T, V> implements Callable<V> {

	private T t;

	public DBCallable(T t) {
		this.t = t;
	}

	@Override
	public V call() throws Exception {
		return call(t);
	}

	public abstract V call(T t);

}

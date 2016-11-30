package com.randioo.randioo_server_base.entity;

public class Ref<T> {
	private T obj = null;

	public T get() {
		return obj;
	}

	public void set(T t) {
		this.obj = t;
	}
}

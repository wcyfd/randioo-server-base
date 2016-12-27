package com.randioo.randioo_server_base.module.record;

public abstract class RefRecordInfo<T> extends AbstractRecordInfo {

	protected T t;

	public RefRecordInfo(T t) {
		this.t = t;
		record();
	}

	public abstract void record();

}

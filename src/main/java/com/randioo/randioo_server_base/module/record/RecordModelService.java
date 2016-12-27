package com.randioo.randioo_server_base.module.record;

import java.lang.reflect.Method;

import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface RecordModelService extends BaseServiceInterface {
	public void addRecord(Recordable recordable, AbstractRecordInfo recordInfo);

	public void addRecord(Recordable recordable, Method method, Object target, Object... params);

	public void clearRecord(Recordable recordable);

	public void resetRecord(Recordable recordable);
}

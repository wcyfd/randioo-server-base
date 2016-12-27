package com.randioo.randioo_server_base.module.record;

import java.lang.reflect.Method;

import com.randioo.randioo_server_base.utils.ReflectUtils;

public class ValueRecordInfo extends AbstractRecordInfo {

	private Method method;
	private Object target;
	private Object[] param;

	public ValueRecordInfo(Method method, Object target, Object... param) {
		this.method = method;
		this.target = target;
		this.param = param;
	}

	@Override
	public void reset() {
		ReflectUtils.invokeMethod(target, method, param);
	}

}

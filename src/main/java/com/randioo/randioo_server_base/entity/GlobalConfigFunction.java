package com.randioo.randioo_server_base.entity;

import java.util.List;
import java.util.Map;

import com.randioo.randioo_server_base.utils.template.Function;

public abstract class GlobalConfigFunction implements Function {

	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public Object apply(Object... params) {
		// TODO Auto-generated method stub
		init((Map<String, Object>) params[0], (List<String>) params[1]);
		return null;
	}

	public abstract void init(Map<String, Object> map, List<String> list);

}

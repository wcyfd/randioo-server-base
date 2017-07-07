package com.randioo.randioo_server_base.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.randioo.randioo_server_base.entity.GlobalConfigFunction;
import com.randioo.randioo_server_base.utils.RandomUtils;

public class GlobleConfig {
	private static Map<String, Object> paramMap = new HashMap<>();
	private static GlobalConfigFunction function = null;

	public enum GlobleEnum {
		LOGIN, PORT, GM, DEBUG, SERVER_KEY
	}

	private GlobleConfig() {

	}

	public static void init(String... strings) {
		List<String> list = Arrays.asList(strings);
		Map<String, Object> _m = paramMap;

		_m.put(GlobleEnum.PORT.name(), Integer.parseInt(list.get(0)));

		_m.put(GlobleEnum.SERVER_KEY.name(), RandomUtils.randowStr(16));
		_m.put(GlobleEnum.LOGIN.name(), false);
		_m.put(GlobleEnum.GM.name(), list.contains(GlobleEnum.GM.name().toLowerCase()));
		_m.put(GlobleEnum.DEBUG.name(), list.contains(GlobleEnum.DEBUG.name().toLowerCase()));
		try {
			if (function != null)
				function.init(_m, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void initParam(GlobalConfigFunction function) {
		GlobleConfig.function = function;
	}

	public static int Int(String key) {
		Object result = paramMap.get(key);
		if (result == null)
			return 0;
		return (int) result;
	}

	public static String String(String key) {
		Object result = paramMap.get(key);
		if (result == null)
			return null;
		return (String) result;

	}

	public static boolean Boolean(String key) {
		Object result = paramMap.get(key);
		if (result == null || !(Boolean) result)
			return false;
		return true;
	}

	public static int Int(GlobleEnum key) {
		return Int(key.name());
	}

	public static String String(GlobleEnum key) {
		return String(key.name());
	}

	public static boolean Boolean(GlobleEnum key) {
		return Boolean(key.name());
	}

	public static void set(GlobleEnum globleEnum, Object value) {
		set(globleEnum.name(), value);
	}

	public static void set(String key, Object value) {
		if (value == null)
			return;
		paramMap.put(key, value);
	}
	
	public static void initBooleanValue(String key, List<String> list) {
		int index = list.indexOf(key);
		if (index != -1) {
			boolean dispatch = Boolean.parseBoolean(list.get(index + 1));
			paramMap.put(key, dispatch);
		}
	}
}

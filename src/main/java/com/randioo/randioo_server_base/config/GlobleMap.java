package com.randioo.randioo_server_base.config;

import java.util.HashMap;
import java.util.Map;

public class GlobleMap {
	protected static Map<String, Object> paramMap = new HashMap<>();

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

	public static void putParam(GlobleParameter parameter) {
		String key = parameter.key;
		String value = parameter.value;

		switch (parameter.type) {
		case GLOBLE_TYPE_BOOLEAN:
			paramMap.put(key, Boolean.valueOf(value));
			break;
		case GLOBLE_TYPE_DOUBLE:
			paramMap.put(key, Double.parseDouble(value));
			break;
		case GLOBLE_TYPE_FLOAT:
			paramMap.put(key, Float.parseFloat(value));
			break;
		case GLOBLE_TYPE_INT:
			paramMap.put(key, Integer.parseInt(value));
			break;
		case GLOBLE_TYPE_STRING:
			paramMap.put(key, value);
			break;
		default:
			break;
		}

	}

	public static void putParam(String key, Object value) {
		paramMap.put(key, value);
	}

	public static String print() {
		return paramMap.toString();
	}

}

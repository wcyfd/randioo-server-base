package com.randioo.randioo_server_base.utils.system;

import java.util.HashSet;
import java.util.Set;

public class GlobleParams {
	public enum DEBUG{
		debug
	}
	private static Set<String> params = new HashSet<>();

	public static Set<String> getParams() {
		return params;
	}

	public static void init(String[] args) {
		for (String arg : args) {
			params.add(arg);
		}
	}
}

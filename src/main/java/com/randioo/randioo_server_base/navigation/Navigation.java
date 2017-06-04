package com.randioo.randioo_server_base.navigation;

import java.util.HashMap;
import java.util.Map;

import com.randioo.randioo_server_base.template.IActionSupport;

public class Navigation {
	private static Map<String, IActionSupport> navigate = new HashMap<>();

	// 根据消息头获取导航
	public static IActionSupport getAction(String name) {
		return navigate.get(name);
	}

}

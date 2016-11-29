package com.randioo.randioo_server_base.navigation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.randioo.randioo_server_base.net.IActionSupport;

public class Navigation {
	private static Map<String, IActionSupport> navigate = new ConcurrentHashMap<>();

	// 根据消息头获取导航
	public static IActionSupport getAction(String name) {
		return navigate.get(name);
	}

}

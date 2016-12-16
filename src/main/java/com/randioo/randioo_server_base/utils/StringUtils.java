package com.randioo.randioo_server_base.utils;

public class StringUtils {
	/**
	 * 检查字符串数据是否改变，属性与方法一一对应
	 * 
	 * @param strings
	 * @return
	 * @author wcy 2016年9月6日
	 */
	public static boolean checkChange(String... strings) {
		int len = strings.length;
		if (len % 2 != 0)// 数组一定是偶数，如果不是，说明有错，还是老老实实存一下吧==
			return true;
		int i = 0;
		while (i < len) {
			String origin = strings[i++];
			String target = strings[i++];
			if (origin == null || !origin.equals(target)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串第一个字符大写
	 * 
	 * @param str
	 * @return
	 * @author wcy 2016年12月16日
	 */
	public static String firstStrToUpperCase(String str) {
		if (str == null || str.equals(""))
			return str;
		String firstStr = str.substring(0, 1);
		return str.replaceFirst(firstStr, firstStr.toUpperCase());
	}
}

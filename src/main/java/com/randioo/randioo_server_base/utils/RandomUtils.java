package com.randioo.randioo_server_base.utils;

import java.util.Random;

public class RandomUtils {
	private final static Random random = new Random(TimeUtils.getNowTime());

	public static final int getRandomNum(int startIndex, int base) {
		if (base <= 0) {
			return 0;
		}
		return startIndex + random.nextInt(base - startIndex + 1);
	}

	public static final int getRandomNum(int limit) {
		if (limit <= 0) {
			return 0;
		}
		return random.nextInt(limit);
	}
	
	/**
	 * 随机生成指定长度的字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String randowStr(int num) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}

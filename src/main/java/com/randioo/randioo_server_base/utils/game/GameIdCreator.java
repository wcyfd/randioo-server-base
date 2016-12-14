package com.randioo.randioo_server_base.utils.game;

/**
 * 生成每次服务器启动后的唯一id
 * 
 * @author wcy 2016年12月13日
 *
 */
public class GameIdCreator {
	private static int id = 0;

	public static synchronized int getId() {
		id++;
		return id;
	}
}

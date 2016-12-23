package com.randioo.randioo_server_base.module.chat;

import java.util.List;
import java.util.Map;

/**
 * 聊天接口
 * 
 * @author wcy 2016年12月21日
 *
 */
public interface Chatable {
	/**
	 * 获得未读取聊天记录数量
	 * 
	 * @return
	 * @author wcy 2016年12月21日
	 */
	public Map<String, Integer> getUnreadChatCountMap();

	/**
	 * 获得已经读取的聊天记录
	 * 
	 * @return
	 * @author wcy 2016年12月21日
	 */
	public Map<String, List<Object>> getHistoryChatMap();

}

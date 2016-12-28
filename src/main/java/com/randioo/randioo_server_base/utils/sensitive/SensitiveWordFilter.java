package com.randioo.randioo_server_base.utils.sensitive;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SensitiveWordFilter {
	@SuppressWarnings({ "rawtypes" })
	private HashMap sensitiveWordMap = new HashMap();
	public static final int MIN_MATCH_TYPE = 1; // 最小匹配规则
	public static final int MAX_MATCH_TYPE = 2; // 最大匹配规则

	@SuppressWarnings("rawtypes")
	public Map getSensitiveWordMap() {
		return sensitiveWordMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		// sensitiveWordMap = new HashMap(keyWordSet.size()); //初始化敏感词容器，减少扩容操作
		String key = null;
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		// 迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while (iterator.hasNext()) {
			key = iterator.next(); // 关键字
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				char keyChar = key.charAt(i); // 转换成char型
				Object wordMap = nowMap.get(keyChar); // 获取

				if (wordMap != null) { // 如果存在该key，直接赋值
					nowMap = (Map) wordMap;
				} else { // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String, String>();
					newWorMap.put("isEnd", "0"); // 不是最后一个
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}

				if (i == key.length() - 1) {
					nowMap.put("isEnd", "1"); // 最后一个
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addSensitiveWordToHashMap(String key) {
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		// 迭代keyWordSet
		nowMap = sensitiveWordMap;
		for (int i = 0; i < key.length(); i++) {
			char keyChar = key.charAt(i); // 转换成char型
			Object wordMap = nowMap.get(keyChar); // 获取

			if (wordMap != null) { // 如果存在该key，直接赋值
				nowMap = (Map) wordMap;
			} else { // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
				newWorMap = new HashMap<String, String>();
				newWorMap.put("isEnd", "0"); // 不是最后一个
				nowMap.put(keyChar, newWorMap);
				nowMap = newWorMap;
			}

			if (i == key.length() - 1) {
				nowMap.put("isEnd", "1"); // 最后一个
			}
		}

	}

	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>
	 * 
	 * @author chenming
	 * @date 2014年4月20日 下午4:31:03
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return，如果存在，则返回敏感词字符的长度，不存在返回0
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes" })
	public int checkSensitiveWord(String txt, int beginIndex, int matchType) {
		boolean flag = false; // 敏感词结束标识位：用于敏感词只有1位的情况
		int matchFlag = 0; // 匹配标识数默认为0
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for (int i = beginIndex; i < txt.length(); i++) {
			word = txt.charAt(i);
			nowMap = (Map) nowMap.get(word); // 获取指定key
			if (nowMap != null) { // 存在，则判断是否为最后一个
				matchFlag++; // 找到相应key，匹配标识+1
				if ("1".equals(nowMap.get("isEnd"))) { // 如果为最后一个匹配规则,结束循环，返回匹配标识数
					flag = true; // 结束标志位为true
					if (MIN_MATCH_TYPE == matchType) { // 最小规则，直接返回,最大规则还需继续查找
						break;
					}
				}
			} else { // 不存在，直接返回
				break;
			}
		}
		// if (matchFlag < 2 || !flag) { // 长度必须大于等于1，为词
		// matchFlag = 0;
		// }

		if (!flag) { // 长度必须大于等于1，为词
			matchFlag = 0;
		}
		return matchFlag;
	}

	/**
	 * 判断文字是否包含敏感字符
	 * 
	 * @author chenming
	 * @date 2014年4月20日 下午4:28:30
	 * @param txt 文字
	 * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return 若包含返回true，否则返回false
	 * @version 1.0
	 */
	public boolean isContaintSensitiveWord(String txt, int matchType) {
		boolean flag = false;
		for (int i = 0; i < txt.length(); i++) {
			int matchFlag = this.checkSensitiveWord(txt, i, matchType); // 判断是否包含敏感字符
			if (matchFlag > 0) { // 大于0存在，返回true
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取文字中的敏感词
	 * 
	 * @author chenming
	 * @date 2014年4月20日 下午5:10:52
	 * @param txt 文字
	 * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return
	 * @version 1.0
	 */
	public Set<String> getSensitiveWord(String txt, int matchType) {
		Set<String> sensitiveWordList = new HashSet<String>();

		for (int i = 0; i < txt.length(); i++) {
			int length = checkSensitiveWord(txt, i, matchType); // 判断是否包含敏感字符
			if (length > 0) { // 存在,加入list中
				sensitiveWordList.add(txt.substring(i, i + length));
				i = i + length - 1; // 减1的原因，是因为for会自增
			}
		}

		return sensitiveWordList;
	}

	public static void main(String[] args) {
		SensitiveWordFilter f1 = new SensitiveWordFilter();
		f1.addSensitiveWordToHashMap("wangchenyang");
		f1.addSensitiveWordToHashMap("wangchendong");
		f1.addSensitiveWordToHashMap("chenchao");
		f1.addSensitiveWordToHashMap("屌");
		f1.addSensitiveWordToHashMap("毛泽东");

		Set<String> ss = f1.getSensitiveWord("yangwangchechenchaodukawangchendong屌iwn毛wangchenyang",
				SensitiveWordFilter.MAX_MATCH_TYPE);
		System.out.println(ss);
	}
}

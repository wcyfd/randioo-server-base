package com.randioo.randioo_server_base.utils.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * 类id生成器
 * 
 * @author wcy 2017年2月13日
 *
 */
@Component
public class IdClassCreator {
	public static final int DEFAULT_INIT_ID = 0;
	private Map<Class<?>, Integer> idMap = new ConcurrentHashMap<>();

	public int getId(Class<?> clazz) {
		Integer id = idMap.get(clazz);
		if (id == null) {
			id = this.createId(clazz, DEFAULT_INIT_ID);
		}

		synchronized (clazz) {
			id++;
			idMap.put(clazz, id);
		}
		return id;

	}

	/**
	 * 初始化id值
	 * 
	 * @param clazz
	 * @param initId
	 * @author wcy 2017年2月13日
	 */
	public void initId(Class<?> clazz, int initId) {
		Integer id = idMap.get(clazz);
		if (id == null) {
			id = this.createId(clazz, initId);
		}

	}

	/**
	 * 创建id
	 * 
	 * @param clazz
	 * @param initId
	 * @return
	 * @author wcy 2017年2月13日
	 */
	private int createId(Class<?> clazz, int initId) {
		synchronized (clazz) {
			Integer id = idMap.get(clazz);
			if (id == null) {
				id = initId;
				idMap.put(clazz, id);
			}
			return id;
		}
	}

	/**
	 * 获得当前id
	 * 
	 * @param clazz
	 * @return
	 * @author wcy 2017年3月21日
	 */
	public int getCurrentId(Class<?> clazz) {
		synchronized (clazz) {
			Integer value = idMap.get(clazz);
			if (value == null)
				value = DEFAULT_INIT_ID;
			return value;
		}
	}
}

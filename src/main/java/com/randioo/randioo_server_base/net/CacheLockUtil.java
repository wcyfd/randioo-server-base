package com.randioo.randioo_server_base.net;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

/**
 *  弱值缓存锁
 * @author xjd
 *
 */
@SuppressWarnings("deprecation")
public class CacheLockUtil {
	private static final Logger log = LoggerFactory
			.getLogger(CacheLockUtil.class);

	private final static ConcurrentMap<String, ReentrantLock> cacheLockMap = new MapMaker()
			.weakValues().makeComputingMap(
					new Function<String, ReentrantLock>() {
						public ReentrantLock apply(String key) {
							return new ReentrantLock();
				
						}
					});

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static ReentrantLock getLock(Class<?> cls, int key) {
		return getReentrantLock(cls, Integer.valueOf(key));
	}

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static ReentrantLock getLock(Class<?> cls, long key) {
		return getReentrantLock(cls, Long.valueOf(key));
	}

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static ReentrantLock getLock(Class<?> cls, String key) {
		return getReentrantLock(cls, key);
	}

	/** 用于监控和调试 */
	protected static int size() {
		return cacheLockMap.size();
	}

	/** 用于监控和调试 */
	protected static boolean isEmpty() {
		return cacheLockMap.isEmpty();
	}

	private static ReentrantLock getReentrantLock(Class<?> cls, Object key) {
		StringBuilder sb = new StringBuilder();
		sb.append(cls.getName()).append("_");
		if (key != null) {
			sb.append(key.toString());
		} else {
			log.error("lock key为null. class : {}", cls.getName());
		}
		return cacheLockMap.get(sb.toString());
	}
}

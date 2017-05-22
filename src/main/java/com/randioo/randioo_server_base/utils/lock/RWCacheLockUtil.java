package com.randioo.randioo_server_base.utils.lock;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import com.randioo.randioo_server_base.net.CacheLockUtil;

public class RWCacheLockUtil {
	private static final Logger log = LoggerFactory.getLogger(CacheLockUtil.class);

	@SuppressWarnings("deprecation")
	private final static ConcurrentMap<String, ReadWriteLock> cacheLockMap = new MapMaker().weakValues()
			.makeComputingMap(new Function<String, ReadWriteLock>() {
				public ReadWriteLock apply(String key) {
					return new ReentrantReadWriteLock();
				}
			});

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static Lock getReadLock(Class<?> cls, int key) {
		return getReadWriteLock(cls, Integer.valueOf(key)).readLock();
	}

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static Lock getReadLock(Class<?> cls, long key) {
		return getReadWriteLock(cls, Long.valueOf(key)).readLock();
	}

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static Lock getReadLock(Class<?> cls, String key) {
		return getReadWriteLock(cls, key).readLock();
	}

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static Lock getWriteLock(Class<?> cls, int key) {
		return getReadWriteLock(cls, Integer.valueOf(key)).writeLock();
	}

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static Lock getWriteLock(Class<?> cls, long key) {
		return getReadWriteLock(cls, Long.valueOf(key)).writeLock();
	}

	/**
	 * 根据传入的对象获取与之对应的锁
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public static Lock getWriteLock(Class<?> cls, String key) {
		return getReadWriteLock(cls, key).writeLock();
	}

	/** 用于监控和调试 */
	protected static int size() {
		return cacheLockMap.size();
	}

	/** 用于监控和调试 */
	protected static boolean isEmpty() {
		return cacheLockMap.isEmpty();
	}

	private static ReadWriteLock getReadWriteLock(Class<?> cls, Object key) {
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

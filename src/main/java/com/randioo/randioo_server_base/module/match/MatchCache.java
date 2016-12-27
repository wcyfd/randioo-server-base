package com.randioo.randioo_server_base.module.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MatchCache {
	private static ScheduledExecutorService scheduleExecutorService = null;
	private static int id = 0;

	private static Lock lock = new ReentrantLock();
	// 匹配信息映射表
	private static Map<Integer, MatchInfo> matchInfoMap = new HashMap<>();
	// 匹配信息id
	private static Set<Integer> matchIdSet = new HashSet<>();
	// 将要删除的缓存列表
	private static List<MatchInfo> needDeleteMatchInfoList = new ArrayList<>();

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		MatchCache.id = id;
	}

	public static Lock getLock() {
		return lock;
	}

	public static Map<Integer, MatchInfo> getMatchInfoMap() {
		return matchInfoMap;
	}

	public static Set<Integer> getMatchIdSet() {
		return matchIdSet;
	}

	public static List<MatchInfo> getNeedDeleteMatchInfoList() {
		return needDeleteMatchInfoList;
	}

	public static ScheduledExecutorService getScheduleExecutorService() {
		return scheduleExecutorService;
	}

	protected static void setScheduleExecutorServiceThreadSize(int threadSize) {
		if (scheduleExecutorService != null) {
			scheduleExecutorService.shutdownNow();
			scheduleExecutorService = null;
		}

		scheduleExecutorService = new ScheduledThreadPoolExecutor(threadSize);
	}

}

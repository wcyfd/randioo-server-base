package com.randioo.randioo_server_base.utils.game.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameMatcher {
	private int threadSize;
	private ScheduledExecutorService scheduleExecutorService = null;
	private MatchHandler matchHandler = null;
	private int id = 0;

	private Lock lock = new ReentrantLock();
	// 匹配信息映射表
	private Map<Integer, MatchInfo> matchInfoMap = new HashMap<>();
	// 匹配信息id
	private Set<Integer> matchIdSet = new HashSet<>();
	// 将要删除的缓存列表
	private List<MatchInfo> needDeleteMatchInfoList = new ArrayList<>();

	public int getThreadSize() {
		return threadSize;
	}

	public void setThreadSize(int threadSize) {
		this.threadSize = threadSize;
		if (scheduleExecutorService != null) {
			scheduleExecutorService.shutdownNow();
			scheduleExecutorService = null;
		}

		scheduleExecutorService = new ScheduledThreadPoolExecutor(threadSize);
	}

	public void setMatchHandler(MatchHandler matchHandler) {
		this.matchHandler = matchHandler;
	}

	private Lock getLock() {
		return lock;
	}

	public void matchRole(MatchRule matchRule) {
		final Lock lock = this.lock;
		lock.lock();
		boolean matchFailed = true;
		try {
			if (!matchRule.isMatchNPC()) {
				for (Integer matchId : matchIdSet) {
					MatchInfo matchInfo = matchInfoMap.get(matchId);
					if (matchInfo == null || matchInfo.isMatchComplete() || matchInfo.isMatchCancel()) {
						continue;
					}
					// 匹配规则
					boolean matchRuleSuccess = matchHandler.matchRule(matchRule, matchInfo);
					// 如果匹配规则成功,匹配人数未满,则可以加入
					if (matchRuleSuccess
							&& matchInfo.getMatchables().size() < matchInfo.getMatchRule().getPlayerCount()) {
						matchFailed = false;

						matchHandler.matchSuccess(matchInfo, matchRule);
						matchInfo.getMatchables().add(matchRule.getMatchTarget());
						matchRule.getMatchTarget().setMatchInfo(matchInfo);

						if (checkMatchComplete(matchInfo)) {
							break;
						}
					} else {
						continue;
					}

				}
			}

			// 如果匹配不成功
			if (matchFailed) {
				MatchInfo matchInfo = matchHandler.createMatchInfo(matchRule);
				id++;
				matchInfo.setMatchId(id);
				matchInfo.setMatchRule(matchRule);
				matchInfo.getMatchables().add(matchRule.getMatchTarget());
				matchRule.getMatchTarget().setMatchInfo(matchInfo);

				if (!matchRule.isMatchNPC()) {
					// 如果需要等待，则进行等待函数
					if (matchHandler.needWaitMatch(matchInfo)) {

						if (!checkMatchComplete(matchInfo)) {
							matchInfo.setScheduleFuture(this.scheduleExecutorService.schedule(new WaitMatchRunnable(
									matchInfo), matchRule.getWaitTime(), matchRule.getWaitUnit()));
							// 匹配没有完成，则加入队列
							matchIdSet.add(matchInfo.getMatchId());
							matchInfoMap.put(matchInfo.getMatchId(), matchInfo);
						}
					}
				} else {
					matchNPC(matchInfo);
				}

			}

			// 删除需要删除的玩家
			for (MatchInfo matchInfo : needDeleteMatchInfoList) {
				matchIdSet.remove(matchInfo.getMatchId());
				matchInfoMap.remove(matchInfo.getMatchId());
			}
			needDeleteMatchInfoList.clear();

		} finally {
			lock.unlock();
		}

	}

	private void matchNPC(MatchInfo matchInfo) {
		for (int i = matchInfo.getMatchables().size(), playerCount = matchInfo.getMatchRule().getPlayerCount(); i < playerCount; i++) {
			MatchRule matchRule = matchHandler.getAutoMatchRole(matchInfo);

			matchHandler.matchSuccess(matchInfo, matchRule);
			matchInfo.getMatchables().add(matchRule.getMatchTarget());
			matchRule.getMatchTarget().setMatchInfo(matchInfo);

			if (checkMatchComplete(matchInfo))
				break;
		}

	}

	private boolean checkMatchComplete(MatchInfo matchInfo) {
		// 匹配完毕，则停止计时器
		if (matchInfo.getMatchables().size() >= matchInfo.getMatchRule().getPlayerCount()) {
			cancelMatchInfoScheduled(matchInfo);

			addNeedDeleteMatchInfo(matchInfo);

			matchInfo.setMatchComplete(true);
			matchHandler.matchComplete(matchInfo);
			return true;
		}
		return false;

	}

	public boolean cancelRole(Matchable role) {
		final Lock lock = this.lock;
		MatchInfo matchInfo = role.getMatchInfo();
		if (matchInfo.isMatchCancel())
			return true;
		if (matchInfo == null ||matchInfo.isMatchComplete()) {
			return false;
		}
		lock.lock();
		try {
			if (matchInfo.isMatchCancel()) {
				return true;
			}
			if (matchInfo.isMatchComplete()) {
				return false;
			}

			// 将此人从匹配列表中删除
			if (matchInfo.getMatchables().size() > 0) {
				matchInfo.getMatchables().remove(role);
				matchHandler.destroyMatchInfo(matchInfo);
				role.setMatchInfo(null);
			}

			// 匹配列表为空时直接取消该匹配
			if (matchInfo.getMatchables().size() <= 0) {
				cancelMatchInfoScheduled(matchInfo);
				matchInfo.setMatchCancel(true);
				addNeedDeleteMatchInfo(matchInfo);
			}
			return true;

		} finally {
			lock.unlock();
		}
	}

	public boolean cancelMatch(Matchable matchable) {
		final Lock lock = this.lock;
		MatchInfo matchInfo = matchable.getMatchInfo();
		//如果已经取消匹配，则返回true
		if(matchInfo.isMatchCancel()){
			return true;
		}
		// 如果已经匹配完成,则不能取消
		if (matchInfo.isMatchComplete()) {
			return false;
		}
		lock.lock();
		if(matchInfo.isMatchCancel()){
			return true;
		}
		if (matchInfo.isMatchComplete()) {
			return false;
		}
		try {
			MatchRule matchRule = matchInfo.getMatchRule();
			List<Matchable> matchables = matchInfo.getMatchables();
			matchables.remove(matchable);
			// 如果删除的人是发起者，并且还有匹配的人，则更换匹配发起者
			if (matchRule.getMatchTarget() == matchable) {
				if(matchables.size()>0){					
					matchRule.setMatchTarget(matchables.get(0));
					matchable.setMatchInfo(null);
				}				
			}
			boolean isAllNPC = false;
			for(Matchable m:matchables){
				isAllNPC = m.isNPC();
			}
			
			if (isAllNPC || matchables.size() == 0) {
				cancelMatchInfoScheduled(matchInfo);
				matchHandler.destroyMatchInfo(matchInfo);
				matchInfo.setMatchCancel(true);
				addNeedDeleteMatchInfo(matchInfo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return true;
	}

	private void cancelMatchInfoScheduled(MatchInfo matchInfo) {
		ScheduledFuture<?> scheduleFuture = matchInfo.getScheduleFuture();
		if (scheduleFuture != null) {
			scheduleFuture.cancel(true);
		}
	}

	private void addNeedDeleteMatchInfo(MatchInfo matchInfo) {
		if (!needDeleteMatchInfoList.contains(matchInfo)) {
			System.out.println("add need delete match info" + needDeleteMatchInfoList.size());
			needDeleteMatchInfoList.add(matchInfo);
		}
	}

	private class WaitMatchRunnable implements Runnable {

		private MatchInfo matchInfo;

		public WaitMatchRunnable(MatchInfo matchInfo) {
			// TODO Auto-generated constructor stub
			this.matchInfo = matchInfo;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 匹配超时

			final Lock lock = getLock();

			if (matchInfo.isMatchComplete() || matchInfo.isMatchCancel())
				return;
			lock.lock();
			try {
				if (matchInfo.isMatchComplete() || matchInfo.isMatchCancel())
					return;

				cancelMatchInfoScheduled(matchInfo);

				matchNPC(matchInfo);
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				lock.unlock();
			}

		}

	}
}

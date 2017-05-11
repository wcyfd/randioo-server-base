package com.randioo.randioo_server_base.module.match;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;

import com.randioo.randioo_server_base.module.BaseService;

public class MatchModelServiceImpl extends BaseService implements MatchModelService {

	private MatchHandler matchHandler;

	@Override
	public void setMatchHandler(MatchHandler matchHandler) {
		this.matchHandler = matchHandler;
	}

	@Autowired
	private MatchConfig matchConfig;

	@Override
	public void init() {
		MatchCache.setScheduleExecutorServiceThreadSize(matchConfig.getThreadSize());
	}

	@Override
	public void matchRole(MatchRule matchRule) {
		final Lock lock = MatchCache.getLock();
		lock.lock();
		boolean matchFailed = true;
		try {
			if (matchRule.getMatchTarget().getMatchInfo() != null) {
				return;
			}
			if (!matchRule.isMatchNPC()) {
				for (Integer matchId : MatchCache.getMatchIdSet()) {
					MatchInfo matchInfo = MatchCache.getMatchInfoMap().get(matchId);
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

						if (this.checkMatchComplete(matchInfo)) {
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
				int id = MatchCache.getId();
				id++;
				MatchCache.setId(id);

				matchInfo.setMatchId(id);
				matchInfo.setMatchRule(matchRule);
				matchInfo.getMatchables().add(matchRule.getMatchTarget());
				matchRule.getMatchTarget().setMatchInfo(matchInfo);

				if (!matchRule.isMatchNPC()) {
					// 如果需要等待，则进行等待函数
					if (matchHandler.needWaitMatch(matchInfo)) {
						// 检查匹配是否完毕
						if (!this.checkMatchComplete(matchInfo)) {
							// 匹配没有完成，则加入队列
							// 先加入数据集
							// 再启动定时器
							MatchCache.getMatchIdSet().add(matchInfo.getMatchId());
							MatchCache.getMatchInfoMap().put(matchInfo.getMatchId(), matchInfo);
							// matchInfo.setScheduleFuture(this.scheduleExecutorService.schedule(new
							// WaitMatchRunnable(
							// matchInfo), matchRule.getWaitTime(),
							// matchRule.getWaitUnit()));
							matchInfo.setScheduleFuture(MatchCache.getScheduleExecutorService().scheduleAtFixedRate(
									new WaitMatchRunnable(matchInfo), 0, 1, matchRule.getWaitUnit()));
						}
					}
				} else {
					matchNPC(matchInfo);
				}

			}

			// 删除需要删除的玩家
			for (MatchInfo matchInfo : MatchCache.getNeedDeleteMatchInfoList()) {
				MatchCache.getMatchIdSet().remove(matchInfo.getMatchId());
				MatchCache.getMatchInfoMap().remove(matchInfo.getMatchId());
			}
			MatchCache.getNeedDeleteMatchInfoList().clear();

		} finally {
			lock.unlock();
		}

	}

	/**
	 * 匹配NPC
	 * 
	 * @param matchInfo
	 */
	private void matchNPC(MatchInfo matchInfo) {
		for (int i = matchInfo.getMatchables().size(), playerCount = matchInfo.getMatchRule().getPlayerCount(); i < playerCount; i++) {
			MatchRule matchRule = matchHandler.getAutoMatchRole(matchInfo);

			matchHandler.matchSuccess(matchInfo, matchRule);
			matchInfo.getMatchables().add(matchRule.getMatchTarget());
			matchRule.getMatchTarget().setMatchInfo(matchInfo);

			if (this.checkMatchComplete(matchInfo))
				break;
		}

	}

	/**
	 * 检查匹配完毕
	 * 
	 * @param matchInfo
	 * @return
	 */
	private boolean checkMatchComplete(MatchInfo matchInfo) {
		// 匹配完毕，则停止计时器
		if (matchInfo.getMatchables().size() >= matchInfo.getMatchRule().getPlayerCount()) {
			cancelMatchInfoScheduled(matchInfo);

			addNeedDeleteMatchInfo(matchInfo);

			matchInfo.setMatchComplete(true);
			matchHandler.matchComplete(matchInfo);
			List<Matchable> matchables = matchInfo.getMatchables();
			// 重置匹配信息
			for (Matchable matchable : matchables) {
				matchable.setMatchInfo(null);
			}
			return true;
		}
		return false;

	}

	@Override
	public void cancelMatch(Matchable matchable) {
		// 锁定操作，直到该操作完成
		final Lock lock = MatchCache.getLock();
		System.out.println(this.getClass().getSimpleName() + " cancelMatch");
		MatchInfo matchInfo = matchable.getMatchInfo();
		// 如果已经取消匹配或者已经匹配完成，则直接返回
		if (matchable.getMatchInfo() == null || matchInfo.isMatchCancel() || matchInfo.isMatchComplete()) {
			return;
		}
		try {
			lock.lock();
			if (matchInfo.isMatchCancel() || matchInfo.isMatchComplete()) {
				return;
			}

			MatchRule matchRule = matchInfo.getMatchRule();
			List<Matchable> matchables = matchInfo.getMatchables();
			matchables.remove(matchable);
			matchable.setMatchInfo(null);
			matchHandler.cancelMatch(matchable);
			// 如果删除的人是发起者，并且还有匹配的人，则更换匹配发起者

			boolean isAllNPC = true;
			Matchable nextMatchTarget = null;
			if (matchRule.getMatchTarget() == matchable) {
				for (Matchable m : matchables) {
					if (!m.isNPC()) {
						isAllNPC = false;
						nextMatchTarget = m;
						break;
					}
				}
			}

			// 如果全部都是npc则取消该匹配
			if (isAllNPC) {
				matchInfo.setMatchCancel(true);
				cancelMatchInfoScheduled(matchInfo);
				matchHandler.destroyMatchInfo(matchInfo);
				addNeedDeleteMatchInfo(matchInfo);
			} else {
				// 替换匹配发起人
				matchRule.setMatchTarget(nextMatchTarget);
				matchHandler.changeStartMatcher(matchable, nextMatchTarget);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 取消匹配信息的定时器
	 * 
	 * @param matchInfo
	 */
	private void cancelMatchInfoScheduled(MatchInfo matchInfo) {
		ScheduledFuture<?> scheduleFuture = matchInfo.getScheduleFuture();
		if (scheduleFuture != null) {
			scheduleFuture.cancel(true);
		}
	}

	/**
	 * 添加需要删除的匹配信息
	 * 
	 * @param matchInfo
	 */
	private void addNeedDeleteMatchInfo(MatchInfo matchInfo) {

		List<MatchInfo> matchInfoList = MatchCache.getNeedDeleteMatchInfoList();
		if (!matchInfoList.contains(matchInfo)) {
			System.out.println("add need delete match info" + matchInfoList.size());
			matchInfoList.add(matchInfo);
		}
	}

	private class WaitMatchRunnable implements Runnable {

		private MatchInfo matchInfo;

		public WaitMatchRunnable(MatchInfo matchInfo) {
			this.matchInfo = matchInfo;
		}

		private int clickCount = 0;

		@Override
		public void run() {
			if (matchInfo.isMatchComplete() || matchInfo.isMatchCancel())
				return;
			final Lock lock = MatchCache.getLock();
			try {
				lock.lock();

				if (matchInfo.isMatchComplete() || matchInfo.isMatchCancel())
					return;

				matchHandler.waitClick(matchInfo, clickCount);

				// 匹配超时
				if (clickCount >= matchInfo.getMatchRule().getWaitTime()) {
					cancelMatchInfoScheduled(matchInfo);

					matchNPC(matchInfo);
				}
				clickCount++;
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				lock.unlock();
			}

		}

	}
}

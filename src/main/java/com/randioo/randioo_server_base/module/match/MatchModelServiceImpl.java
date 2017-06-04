package com.randioo.randioo_server_base.module.match;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;

import com.randioo.randioo_server_base.lock.CacheLockUtil;
import com.randioo.randioo_server_base.module.match.MatchRule.MatchState;
import com.randioo.randioo_server_base.scheduler.EventScheduler;
import com.randioo.randioo_server_base.service.BaseService;
import com.randioo.randioo_server_base.template.EntityRunnable;
import com.randioo.randioo_server_base.utils.TimeUtils;

public class MatchModelServiceImpl extends BaseService implements MatchModelService {

	private ExecutorService executor = null;
	private ExecutorService matchSuccessExecutor = null;
	private MatchRuleCache matchRuleCache = null;

	@Autowired
	private EventScheduler eventScheduler;

	private MatchHandler matchHandler;

	@Override
	public void initService() {
		executor = Executors.newSingleThreadScheduledExecutor();
		matchSuccessExecutor = Executors.newCachedThreadPool();
		matchRuleCache = new MatchRuleCache();
	}

	@Override
	public void setMatchHandler(MatchHandler matchHandler) {
		this.matchHandler = matchHandler;
	}

	@Override
	public void matchRole(MatchRule matchRule) {
		matchRule.setState(MatchState.MATCH_READY);
		// 添加匹配信息
		matchRuleCache.getMatchRuleMap().put(matchRule.getId(), matchRule);
		// 设置超时定时器
		executor.submit(new EntityRunnable<MatchRule>(matchRule) {

			@Override
			public void run(MatchRule entity) {
				try {
					// 由于异步加入，所以加入之前先检查一次是否可以删除
					Lock lock = getLock(entity.getId());
					try {
						lock.lock();
						if (checkDelete(entity)) {
							return;
						}
					} finally {
						lock.unlock();
					}
					matchRuleCache.getMatchTempMap().put(entity.getId(), entity);

					Set<String> matchRuleIdSet = new HashSet<>(matchRuleCache.getMatchRuleMap().keySet());

					boolean matchSuccess = false;
					for (String id : matchRuleIdSet) {
						MatchRule matchRule = matchRuleCache.getMatchRuleMap().get(id);
						// 不能匹配自己
						if (matchRule.getId().equals(entity.getId())) {
							continue;
						}

						// 先检查规则，通过了在考虑同步问题
						if (!matchHandler.checkMatchRule(entity, matchRule))
							continue;

						// 检查是否要删除,检查第二次,主要还是为了提高锁同步的必要性,由于检查匹配规则的耗时可能非常的长,但是玩家可以随取消匹配的
						// 只检查自己是否取消匹配，如果取消匹配了，则下面的人不可能匹配上，直接下一个人
						if (checkDelete(entity))
							break;

						// 规则匹配没有问题则加入到缓存
						matchRuleCache.getMatchTempMap().put(matchRule.getId(), matchRule);

						// 匹配到的人的集合与当前人的规则进行比较
						if (!matchHandler.checkArriveMaxCount(entity, matchRuleCache.getMatchTempMap()))
							continue;

						initLocks();
						try {
							lockSet_Lock();

							for (MatchRule rule : matchRuleCache.getMatchTempMap().values()) {
								if (checkDelete(rule))
									matchRuleCache.getNeedDeleteIdTempSet().add(rule.getId());
							}

							// 如果有要删除的则再找下一个人
							if (matchRuleCache.getNeedDeleteIdTempSet().size() > 0) {
								for (String deleteId : matchRuleCache.getNeedDeleteIdTempSet())
									matchRuleCache.getMatchTempMap().remove(deleteId);
								continue;
							}

							// 匹配成功
							for (MatchRule rule : matchRuleCache.getMatchTempMap().values())
								rule.setState(MatchState.MATCH_SUCCESS);

							matchSuccess = true;
							break;

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							lockSet_Unlock();
							matchRuleCache.getLocksTempMap().clear();
							matchRuleCache.getNeedDeleteIdTempSet().clear();
						}
					}
					if (matchSuccess) {
						matchRuleCache.getDeleteMatchRuleIdSet().addAll(matchRuleCache.getMatchTempMap().keySet());
						Map<String, MatchRule> copyTempMap = new HashMap<>(matchRuleCache.getMatchTempMap());
						try {
							matchSuccessExecutor.submit(new EntityRunnable<Map<String, MatchRule>>(copyTempMap) {

								@Override
								public void run(Map<String, MatchRule> entity) {
									matchHandler.matchSuccess(entity);
								}

							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 删除作废的匹配
					for (String ruleId : matchRuleCache.getDeleteMatchRuleIdSet())
						matchRuleCache.getMatchRuleMap().remove(ruleId);

					matchRuleCache.getMatchTempMap().clear();
					matchRuleCache.getDeleteMatchRuleIdSet().clear();
				}
			}
		});

		// 如果没有设置等待时间则不进行倒计时
		if (matchRule.getWaitTime() == 0)
			return;

		MatchTimeEvent timeEvent = new MatchTimeEvent(matchRule) {

			@Override
			public void outOfTime(MatchRule matchRule) {
				if (checkDelete(matchRule))
					return;

				Lock lock = getLock(matchRule.getId());
				try {
					lock.lock();
					if (checkDelete(matchRule))
						return;

					matchHandler.outOfTime(matchRule);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		};

		int endTime = TimeUtils.getNowTime() + matchRule.getWaitTime();
		timeEvent.setEndTime(endTime);
		// 发送定时器
		eventScheduler.addEvent(timeEvent);
	}

	/**
	 * 检查是否删除
	 * 
	 * @param matchRule
	 * @return
	 */
	private boolean checkDelete(MatchRule matchRule) {
		if (matchRule == null)
			return true;

		MatchState state = matchRule.getState();
		if (state != MatchState.MATCH_CANCEL && state != MatchState.MATCH_SUCCESS)
			return false;

		matchRuleCache.getDeleteMatchRuleIdSet().add(matchRule.getId());

		return true;
	}

	@Override
	public void cancelMatch(String ruleId) {
		MatchRule matchRule = matchRuleCache.getMatchRuleMap().get(ruleId);

		if (checkDelete(matchRule))
			return;
		Lock lock = getLock(ruleId);
		try {
			lock.lock();

			if (checkDelete(matchRule))
				return;

			matchRule.setState(MatchState.MATCH_CANCEL);
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 获得锁
	 * 
	 * @param id
	 * @return
	 * @author wcy 2017年5月26日
	 */
	private Lock getLock(String id) {
		return CacheLockUtil.getLock(MatchRule.class, id);
	}

	private void initLocks() {
		Map<String, MatchRule> map = matchRuleCache.getMatchTempMap();
		for (MatchRule matchRule : map.values())
			matchRuleCache.getLocksTempMap().add(getLock(matchRule.getId()));
	}

	private void lockSet_Lock() {
		for (Lock lock : matchRuleCache.getLocksTempMap())
			lock.lock();

	}

	private void lockSet_Unlock() {
		for (Lock lock : matchRuleCache.getLocksTempMap()) {
			lock.unlock();
		}
	}

}

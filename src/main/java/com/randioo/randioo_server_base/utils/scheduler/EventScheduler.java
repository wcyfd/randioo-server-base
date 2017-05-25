package com.randioo.randioo_server_base.utils.scheduler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.randioo.randioo_server_base.db.DBRunnable;
import com.randioo.randioo_server_base.utils.TimeUtils;

public class EventScheduler implements SchedulerInterface {

	private int slowTime = 15;
	private int quickTime = 1;
	private int slowTimeDeltaTime = 2;

	public void setSlowTime(int slowTime) {
		this.slowTime = slowTime;
	}

	public void setQuickTime(int quickTime) {
		this.quickTime = quickTime;
	}

	public void setSlowTimeDeltaTime(int slowTimeDeltaTime) {
		this.slowTimeDeltaTime = slowTimeDeltaTime;
	}

	private ScheduledExecutorService eventService = Executors.newScheduledThreadPool(1);
	private ExecutorService handlerService = Executors.newSingleThreadExecutor();
	private Map<TimeEvent, TimeEvent> slowSet = new ConcurrentHashMap<>();
	private Map<TimeEvent, TimeEvent> quickSet = new ConcurrentHashMap<>();

	@Override
	public void start() {
		eventService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				Set<TimeEvent> deleteSet = new HashSet<>();
				for (TimeEvent timeEvent : quickSet.values()) {
					int nowTime = TimeUtils.getNowTime();
					int endTime = timeEvent.getEndTime();
					if (nowTime >= endTime) {
						deleteSet.add(timeEvent);
						handlerService.submit(new DBRunnable<TimeEvent>(timeEvent) {

							@Override
							public void run(TimeEvent entity) {
								try {
									entity.update(entity);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						});
					}
				}

				// 移除
				for (TimeEvent timeEvent : deleteSet) {
					quickSet.remove(timeEvent);
				}
			}
		}, 1, quickTime, TimeUnit.SECONDS);

		eventService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				Set<TimeEvent> deleteSet = new HashSet<>();
				for (TimeEvent timeEvent : slowSet.values()) {
					if (timeEvent.getEndTime() - TimeUtils.getNowTime() <= (slowTime + slowTimeDeltaTime)) {
						quickSet.put(timeEvent, timeEvent);
						deleteSet.add(timeEvent);
					}
				}

				// 移除
				for (TimeEvent timeEvent : deleteSet) {
					slowSet.remove(timeEvent);
				}

			}
		}, 0, slowTime, TimeUnit.SECONDS);
	}

	public void addEvent(TimeEvent timeEvent) {
		if (timeEvent.getEndTime() - TimeUtils.getNowTime() < this.slowTime) {
			quickSet.put(timeEvent, timeEvent);
		} else {
			slowSet.put(timeEvent, timeEvent);
		}
	}

	@Override
	public void shutdown(long timeout, TimeUnit unit) throws Exception {
		eventService.shutdown();
		while (!eventService.awaitTermination(timeout, unit)) {
		}
	}

}

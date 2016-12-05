package com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.randioo.randioo_server_base.utils.game.game_type.GameBase;
import com.randioo.randioo_server_base.utils.game.game_type.GameHandler;

public class RTSGameStarter {
	private int threadSize;
	private ScheduledExecutorService scheduleExecutorService = null;
	private GameHandler gameHandler = null;

	public void setThreadSize(int threadSize) {
		this.threadSize = threadSize;
		if (scheduleExecutorService != null) {
			scheduleExecutorService.shutdownNow();
			scheduleExecutorService = null;
		}

		scheduleExecutorService = new ScheduledThreadPoolExecutor(threadSize);
	}

	public int getThreadSize() {
		return threadSize;
	}

	public void setGameHandler(GameHandler gameHandler) {
		this.gameHandler = gameHandler;
	}

	/**
	 * 
	 * @param RTSGame
	 * @param keyFrameNeedTime 关键帧与关键帧之间的毫秒数
	 * @author wcy 2016年11月29日
	 */
	public void startRTSGame(RTSGame RTSGame, int keyFrameNeedTime) {
		this.startRTSGame(RTSGame, keyFrameNeedTime, TimeUnit.MILLISECONDS);
	}

	/**
	 * 
	 * @param RTSGame
	 * @param keyFrameNeedTime
	 * @param timeUnit
	 * @author wcy 2016年12月1日
	 */
	public void startRTSGame(RTSGame RTSGame, int keyFrameNeedTime, TimeUnit timeUnit) {
		ScheduledFuture<?> scheduleFuture = scheduleExecutorService.scheduleAtFixedRate(new GameRunnable(RTSGame) {

			@Override
			public void run() {
				gameHandler.gameLogic(RTSGame);
			}

		}, 0, keyFrameNeedTime, timeUnit);
		RTSGame.setScheduledFuture(scheduleFuture);
	}

}

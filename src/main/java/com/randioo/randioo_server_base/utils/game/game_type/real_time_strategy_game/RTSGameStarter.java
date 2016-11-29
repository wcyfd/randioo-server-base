package com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

	public void startGame(RTSGame RTSGame, int keyFrameNeedTime) {
		ScheduledFuture<?> scheduleFuture = scheduleExecutorService.scheduleAtFixedRate(new GameRunnable(RTSGame) {

			@Override
			public void run() {
				gameHandler.gameLogic(gameScheduled);
			}

		}, 0, keyFrameNeedTime, TimeUnit.MILLISECONDS);
		RTSGame.setScheduleFuture(scheduleFuture);
	}

}

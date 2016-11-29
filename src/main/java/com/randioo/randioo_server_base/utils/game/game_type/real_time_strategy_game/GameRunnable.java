package com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game;



public abstract class GameRunnable implements Runnable {
	protected RTSGame gameScheduled;

	public GameRunnable(RTSGame gameScheduled) {
		this.gameScheduled = gameScheduled;
	}

	@Override
	public abstract void run();

}

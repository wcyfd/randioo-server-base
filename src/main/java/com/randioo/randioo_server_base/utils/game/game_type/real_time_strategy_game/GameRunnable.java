package com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game;



public abstract class GameRunnable implements Runnable {
	protected RTSGame RTSGame;

	public GameRunnable(RTSGame RTSGame) {
		this.RTSGame = RTSGame;
	}

	@Override
	public abstract void run();

}

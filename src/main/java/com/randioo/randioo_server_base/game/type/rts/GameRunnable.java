package com.randioo.randioo_server_base.game.type.rts;



public abstract class GameRunnable implements Runnable {
	protected RTSGame RTSGame;

	public GameRunnable(RTSGame RTSGame) {
		this.RTSGame = RTSGame;
	}

	@Override
	public abstract void run();

}

package com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game;

import java.util.concurrent.ScheduledFuture;

import com.randioo.randioo_server_base.entity.ActionQueue;
import com.randioo.randioo_server_base.utils.game.game_type.GameBase;

public class RTSGame implements GameBase {
	private ScheduledFuture<?> scheduledFuture = null;
	private ActionQueue actionQueue = new ActionQueue();
	private int frameNumber;
	private int nextFrameNumber;

	public ActionQueue getActionQueue() {
		return actionQueue;
	}

	public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		this.scheduledFuture = scheduledFuture;
	}

	public ScheduledFuture<?> getScheduledFuture() {
		return scheduledFuture;
	}

	public int getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}

	public int getNextFrameNumber() {
		return nextFrameNumber;
	}

	public void setNextFrameNumber(int nextFrameNumber) {
		this.nextFrameNumber = nextFrameNumber;
	}


}

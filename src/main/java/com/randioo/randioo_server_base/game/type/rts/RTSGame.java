package com.randioo.randioo_server_base.game.type.rts;

import java.util.concurrent.ScheduledFuture;

import com.randioo.randioo_server_base.entity.ActionQueue;
import com.randioo.randioo_server_base.game.type.GameBase;

public class RTSGame implements GameBase {
	private ScheduledFuture<?> scheduledFuture = null;
	private ActionQueue actionQueue = new ActionQueue();
	private int currentFrameNumber;
//	private int nextFrameNumber;
	protected int addDeltaFrame;

	public ActionQueue getActionQueue() {
		return actionQueue;
	}

	public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		this.scheduledFuture = scheduledFuture;
	}

	public ScheduledFuture<?> getScheduledFuture() {
		return scheduledFuture;
	}

	public int getCurrentFrameNumber() {
		return currentFrameNumber;
	}

	public void setCurrentFrameNumber(int currentFrameNumber) {
		this.currentFrameNumber = currentFrameNumber;
	}

	public int getAddDeltaFrame() {
		return addDeltaFrame;
	}

//	public int getNextFrameNumber() {
//		return nextFrameNumber;
//	}
//
//	public void setNextFrameNumber(int nextFrameNumber) {
//		this.nextFrameNumber = nextFrameNumber;
//	}

}

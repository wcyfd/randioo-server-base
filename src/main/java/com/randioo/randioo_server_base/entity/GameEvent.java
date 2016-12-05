package com.randioo.randioo_server_base.entity;

import com.google.protobuf.GeneratedMessage;

public class GameEvent implements Comparable<GameEvent> {
	private int executeFrameIndex;
	private GeneratedMessage action;

	public GameEvent() {
	}

	public GameEvent(GeneratedMessage action, int executeFrameIndex) {
		this.setExecuteFrameIndex(executeFrameIndex);
		this.action = action;
	}

	public int getExecuteFrameIndex() {
		return executeFrameIndex;
	}

	public void setExecuteFrameIndex(int executeFrameIndex) {
		this.executeFrameIndex = executeFrameIndex;
	}

	public GeneratedMessage getAction() {
		return action;
	}

	public void setAction(GeneratedMessage action) {
		this.action = action;
	}

	@Override
	public int compareTo(GameEvent o) {
		return Integer.compare(executeFrameIndex, o.executeFrameIndex);
	}

}

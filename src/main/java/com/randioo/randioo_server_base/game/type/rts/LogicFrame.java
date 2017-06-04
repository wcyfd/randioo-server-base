package com.randioo.randioo_server_base.game.type.rts;

import java.util.ArrayList;
import java.util.List;

public class LogicFrame {
	/** 帧索引 */
	private int frameIndex;
	/** 事件列表 */
	private List<Object> gameActions = new ArrayList<>();

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public List<Object> getGameActions() {
		return gameActions;
	}
}

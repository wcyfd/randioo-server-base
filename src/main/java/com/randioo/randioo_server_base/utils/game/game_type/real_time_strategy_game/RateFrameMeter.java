package com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game;

import java.util.ArrayList;
import java.util.List;

import com.randioo.randioo_server_base.entity.ActionQueue;
import com.randioo.randioo_server_base.entity.GameEvent;
import com.randioo.randioo_server_base.utils.template.Function;

public class RateFrameMeter {



	public void sendKeyFrame(RTSGame game, Function function) {
		// send1(game, function);
		send2(game, function);
	}

	// private void send1(RTSGame game, Function function) {
	// ActionQueue actionQueue = game.getActionQueue();
	// int nextFrameNumber = game.getNextFrameNumber();
	// int currentFrameNumber = game.getFrameNumber();
	//
	// // 提出至目标值
	// List<GameEvent> gameEvents = actionQueue.pollAll(nextFrameNumber);
	//
	// // 用于加入没有刷到的过时帧事件
	// List<GameEvent> deleteGameEvents = new ArrayList<>(gameEvents.size());
	//
	// List<LogicFrame> logicFrames = new ArrayList<>();
	// for (int frame = currentFrameNumber; frame < nextFrameNumber; frame++) {
	// LogicFrame logicFrame = new LogicFrame();
	// logicFrame.setFrameIndex(frame);
	// for (int j = 0; j < gameEvents.size(); j++) {
	// GameEvent gameEvent = gameEvents.get(j);
	// int frameIndex = gameEvent.getExecuteFrameIndex();
	// if (frameIndex <= frame) {
	// Object gameAction = gameEvent.getAction();
	// logicFrame.getGameActions().add(gameAction);
	//
	// deleteGameEvents.add(gameEvent);
	// }
	// }
	// for (GameEvent event : deleteGameEvents) {
	// gameEvents.remove(event);
	// }
	// logicFrames.add(logicFrame);
	//
	// }
	// game.setFrameNumber(nextFrameNumber);
	// game.setNextFrameNumber(nextFrameNumber + game.getAddDeltaFrame());
	// if (logicFrames.size() == 0)
	// return;
	// function.apply(logicFrames);
	// }

	private void send2(RTSGame game, Function function) {
		ActionQueue actionQueue = game.getActionQueue();
		int currentFrameNumber = game.getCurrentFrameNumber();
		// int nextFrameNumber = game.getNextFrameNumber();
		int nextFrameNumber = currentFrameNumber + game.getAddDeltaFrame();

		// 提出至目标值
		List<GameEvent> gameEvents = actionQueue.pollAll(nextFrameNumber);

		// 用于加入没有刷到的过时帧事件
		List<GameEvent> deleteGameEvents = new ArrayList<>(gameEvents.size());

		List<LogicFrame> logicFrames = new ArrayList<>();
		for (int frame = currentFrameNumber; frame < nextFrameNumber; frame++) {
			LogicFrame logicFrame = new LogicFrame();
			logicFrame.setFrameIndex(frame);
			for (int j = 0; j < gameEvents.size(); j++) {
				GameEvent gameEvent = gameEvents.get(j);
				int frameIndex = gameEvent.getExecuteFrameIndex();
				if (frameIndex <= frame) {
					Object gameAction = gameEvent.getAction();
					logicFrame.getGameActions().add(gameAction);

					deleteGameEvents.add(gameEvent);
				}
			}
			for (GameEvent event : deleteGameEvents) {
				gameEvents.remove(event);
			}
			logicFrames.add(logicFrame);

		}
		game.setCurrentFrameNumber(nextFrameNumber);
		// game.setNextFrameNumber(nextFrameNumber + game.getAddDeltaFrame());
		if (logicFrames.size() == 0)
			return;
		function.apply(logicFrames);
	}
}

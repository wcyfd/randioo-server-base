package com.randioo.randioo_server_base.utils.game.game_type.real_time_strategy_game;

import java.util.concurrent.ScheduledFuture;

import com.randioo.randioo_server_base.utils.game.game_type.GameBase;

public interface RTSGame extends GameBase{
	public ScheduledFuture<?> getScheduleFuture();

	public void setScheduleFuture(ScheduledFuture<?> scheduleFuture);
}

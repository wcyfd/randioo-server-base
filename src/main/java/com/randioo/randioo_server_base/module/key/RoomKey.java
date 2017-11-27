package com.randioo.randioo_server_base.module.key;

public class RoomKey extends Key {
    private int gameId;

    public RoomKey() {
        this.gameId = -1;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    protected void reset() {
        gameId = -1;
    }

    public String getName() {
        return String.valueOf(getValue());
    }
}

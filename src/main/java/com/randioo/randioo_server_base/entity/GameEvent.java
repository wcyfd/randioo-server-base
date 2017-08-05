package com.randioo.randioo_server_base.entity;

import com.google.protobuf.GeneratedMessage;

/**
 * 游戏事件
 * 
 * @author wcy 2017年8月5日
 *
 */
public class GameEvent implements Comparable<GameEvent> {
    /** 帧id */
    private int executeFrameIndex;
    /** 具体执行的动作 */
    private Object action;

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

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        this.action = action;
    }

    @Override
    public int compareTo(GameEvent o) {
        return Integer.compare(executeFrameIndex, o.executeFrameIndex);
    }

}

package cn.jja8.limbo.eventWork.event.player;

import cn.jja8.limbo.Player;

/**
 * 玩家事件
 * */
public abstract class PlayerCancelableEvent extends PlayerEvent {
   boolean isCancel = false;

    public PlayerCancelableEvent(Player player) {
        super(player);
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }
}

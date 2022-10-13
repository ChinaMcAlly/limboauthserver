package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;

/**
 * 玩家事件
 * @author jja8
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

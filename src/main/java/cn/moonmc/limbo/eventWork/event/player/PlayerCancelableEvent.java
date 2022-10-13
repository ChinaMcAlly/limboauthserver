package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.eventWork.event.Cancelable;

/**
 * 玩家可取消事件
 * @author jja8
 * */
public abstract class PlayerCancelableEvent extends PlayerEvent implements Cancelable {
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

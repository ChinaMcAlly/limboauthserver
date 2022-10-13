package cn.moonmc.jja8.limbo.eventWork.event.player;

import cn.moonmc.jja8.limbo.Player;
import cn.moonmc.jja8.limbo.eventWork.event.Event;

/**
 * 玩家事件
 * */
public abstract class PlayerEvent implements Event {
    Player player;
    public PlayerEvent(Player player) {
        this.player = player;
    }
    /**
     * 获得触发此事件的玩家
     * */
    public Player getPlayer() {
        return player;
    }
}

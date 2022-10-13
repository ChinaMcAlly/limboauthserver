package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.eventWork.event.Event;

/**
 * 玩家事件
 * @author jja8
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

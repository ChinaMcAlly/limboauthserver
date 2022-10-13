package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;

/**
 * 玩家退出事件
 * @author jja8
 * */
public class PlayerQuitEvent extends PlayerEvent{
    public PlayerQuitEvent(Player player) {
        super(player);
    }
}

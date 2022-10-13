package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;

/**
 * 玩家进入服务器事件
 * @author jja8
 * */
public class PlayerJoinEvent extends PlayerEvent{
    public PlayerJoinEvent(Player player) {
        super(player);
    }
}

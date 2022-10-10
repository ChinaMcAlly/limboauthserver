package cn.jja8.limbo.eventWork.event.player;

import cn.jja8.limbo.Player;

public class PlayerJoinEvent extends PlayerEvent{
    public PlayerJoinEvent(Player player) {
        super(player);
    }
}

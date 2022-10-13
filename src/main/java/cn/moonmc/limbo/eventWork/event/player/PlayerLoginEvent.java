package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;

public class PlayerLoginEvent extends PlayerEvent{
    public PlayerLoginEvent(Player player) {
        super(player);
    }
}

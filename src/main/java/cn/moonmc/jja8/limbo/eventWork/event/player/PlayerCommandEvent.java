package cn.moonmc.jja8.limbo.eventWork.event.player;

import cn.moonmc.jja8.limbo.Player;

/**
 * 玩家命令事件
 * */
public class PlayerCommandEvent extends PlayerEvent {
    String command;

    public PlayerCommandEvent(Player player, String command) {
        super(player);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}

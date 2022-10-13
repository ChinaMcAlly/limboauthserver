package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;

/**
 * 玩家命令事件
 * @author jja8
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

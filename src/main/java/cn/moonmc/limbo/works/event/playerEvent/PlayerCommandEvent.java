package cn.moonmc.limbo.works.event.playerEvent;

import cn.moonmc.limbo.Player;
import lombok.Getter;

/**
 * 玩家命令事件
 *
 * @author jja8
 */
public class PlayerCommandEvent extends PlayerEvent {
    @Getter
    String command;

    public PlayerCommandEvent(Player player, String command) {
        super(player);
        this.command = command;
    }
}

package cn.moonmc.limbo.works.event.playerEvent;

import cn.moonmc.limbo.Player;
import lombok.Getter;

/**
 * 玩家聊天事件
 *
 * @author jja8
 */
public class PlayerChatEvent extends PlayerEvent {
    /**
     * 获取玩家的聊天信息
     */
    @Getter
    String messages;

    public PlayerChatEvent(Player player, String messages) {
        super(player);
        this.messages = messages;
    }
}

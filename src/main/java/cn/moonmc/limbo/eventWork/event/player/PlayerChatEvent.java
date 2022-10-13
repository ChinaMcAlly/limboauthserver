package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;
import lombok.Getter;

/**
 * 玩家聊天事件
 * @author jja8
 * */
public class PlayerChatEvent extends PlayerEvent {
    /**
     * 获取玩家的聊天信息
     * */
    @Getter
    String chat;

    public PlayerChatEvent(Player player, String chat) {
        super(player);
        this.chat = chat;
    }
}

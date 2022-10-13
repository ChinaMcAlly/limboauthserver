package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;

/**
 * 玩家聊天事件
 * @author jja8
 * */
public class PlayerChatEvent extends PlayerEvent {
    String chat;

    public PlayerChatEvent(Player player, String chat) {
        super(player);
        this.chat = chat;
    }

    public String getChat() {
        return chat;
    }
}

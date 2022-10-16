package cn.moonmc.limbo.works.event.playerEvent;

import cn.moonmc.limbo.Player;
import lombok.Getter;

/**
 * 玩家点击物品栏格子事件
 *
 * @author jja8
 * 点击类型太复杂了，就先这样吧，知道格子应该够用了QAQ
 */
public class PlayerClickContainer extends PlayerEvent {
    /**
     * 被点击的格子
     */
    @Getter
    short slot;
    @Getter
    int windowId;


    public PlayerClickContainer(Player player, short slot, int windowId) {
        super(player);
        this.slot = slot;
        this.windowId = windowId;
    }
}

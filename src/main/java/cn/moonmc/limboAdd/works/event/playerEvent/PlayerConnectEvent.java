package cn.moonmc.limboAdd.works.event.playerEvent;

import cn.moonmc.limboAdd.works.entity.Player;

/**
 * 玩家进入服务器，但是还没完成握手时触发此事件
 * @author jja8
 * */
public class PlayerConnectEvent extends PlayerEvent{
    public PlayerConnectEvent(Player player) {
        super(player);
    }
}

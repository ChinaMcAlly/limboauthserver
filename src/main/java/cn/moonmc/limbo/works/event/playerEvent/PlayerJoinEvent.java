package cn.moonmc.limbo.works.event.playerEvent;

import cn.moonmc.limbo.Player;

/***
 * 玩家进入服务器事件
 * @author 夜光
 * */
public class PlayerJoinEvent extends PlayerEvent{
    public PlayerJoinEvent(Player player) {
        super(player);
    }
}

package cn.moonmc.limboAdd.works.event.playerEvent;

import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.Event;
import lombok.Getter;

/**
 * 玩家事件
 * @author jja8
 * */
public abstract class PlayerEvent implements Event {
    /**
     * 获得触发此事件的玩家
     * */
    @Getter
    Player player;
    public PlayerEvent(Player player) {
        this.player = player;
    }

}

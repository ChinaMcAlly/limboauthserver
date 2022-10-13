package cn.moonmc.limbo.eventWork.event.player;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.eventWork.event.Event;
import lombok.Data;
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

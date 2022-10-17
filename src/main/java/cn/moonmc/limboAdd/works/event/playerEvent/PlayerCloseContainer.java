package cn.moonmc.limboAdd.works.event.playerEvent;

import cn.moonmc.limboAdd.works.entity.Player;
import lombok.Getter;

/**
 * 玩家关闭容器事件
 * @author jja8
 * */
public class PlayerCloseContainer extends PlayerEvent {
    @Getter
    int windowID;

    public PlayerCloseContainer(Player player, int windowID) {
        super(player);
        this.windowID = windowID;
    }
}

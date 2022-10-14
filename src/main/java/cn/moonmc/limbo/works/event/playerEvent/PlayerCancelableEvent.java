package cn.moonmc.limbo.works.event.playerEvent;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.works.event.Cancelable;
import lombok.Getter;
import lombok.Setter;

/**
 * 玩家可取消事件
 * @author jja8
 * */
public abstract class PlayerCancelableEvent extends PlayerEvent implements Cancelable {
    @Getter @Setter
   boolean isCancel = false;

    public PlayerCancelableEvent(Player player) {
        super(player);
    }
}

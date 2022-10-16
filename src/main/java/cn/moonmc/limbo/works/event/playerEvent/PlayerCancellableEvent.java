package cn.moonmc.limbo.works.event.playerEvent;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.works.event.Cancellable;
import lombok.Getter;
import lombok.Setter;

/**
 * 玩家可取消事件
 *
 * @author jja8
 */
public abstract class PlayerCancellableEvent extends PlayerEvent implements Cancellable {
    @Getter
    @Setter
    boolean isCancelled = false;

    public PlayerCancellableEvent(Player player) {
        super(player);
    }
}

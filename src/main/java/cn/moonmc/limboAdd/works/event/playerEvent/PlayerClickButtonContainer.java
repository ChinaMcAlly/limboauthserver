package cn.moonmc.limboAdd.works.event.playerEvent;

import cn.moonmc.limboAdd.works.entity.Player;
import lombok.Getter;

/**
 * @author CNLuminous 2022/10/26
 */
public class PlayerClickButtonContainer extends PlayerEvent{

    @Getter
    private Integer buttonId;
    @Getter
    Integer windowId;

    public PlayerClickButtonContainer(Player player, Integer buttonId, Integer windowId) {
        super(player);
        this.buttonId = buttonId;
        this.windowId = windowId;
    }



}

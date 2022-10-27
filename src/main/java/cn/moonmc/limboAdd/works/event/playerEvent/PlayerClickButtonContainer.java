package cn.moonmc.limboAdd.works.event.playerEvent;

import cn.moonmc.limboAdd.works.entity.Player;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CNLuminous 2022/10/26
 */
public class PlayerClickButtonContainer extends PlayerEvent{

    @Getter
    private Integer buttonId;

    public PlayerClickButtonContainer(Player player,Integer buttonId) {
        super(player);
        this.buttonId = buttonId;
    }



}

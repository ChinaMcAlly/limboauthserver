package cn.moonmc.limboAdd.works.event.playerEvent;

import cn.moonmc.limboAdd.works.entity.Player;
import lombok.Getter;

/**
 * 铁砧每次修改物品名称都会触发
 * @author jja8
 * */
public class PlayerRenameItem extends PlayerEvent {
    /**
     * 获取被修改的名称
     * */
    @Getter
    String name;

    public PlayerRenameItem(Player player, String name) {
        super(player);
        this.name = name;
    }
}

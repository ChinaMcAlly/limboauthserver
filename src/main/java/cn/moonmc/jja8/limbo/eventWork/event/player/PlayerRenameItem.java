package cn.moonmc.jja8.limbo.eventWork.event.player;

import cn.moonmc.jja8.limbo.Player;

/**
 * 铁砧每次修改物品名称都会触发
 * */
public class PlayerRenameItem extends PlayerEvent {
    String name;

    public PlayerRenameItem(Player player, String name) {
        super(player);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

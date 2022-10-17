package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.packets.out.PacketSetContainerSlot;
import cn.moonmc.limboAdd.works.entity.Player;

/**
 * 代表玩家物品栏，背包里物品设置的功能就不写了awa反正也没用。就写一个设置光标上物品的功能够用了awa。
 * 也不记录光标上的物品了，反正也没用。至于玩家把物品放到什么地方了光标上还有没有物品啥的，知道这些干啥，反正也没用。
 * @author jja8
 * */
public class PlayerInventory implements Inventory{
    final Player player;

    /**
     * 请使用Player.getPlayerInventory();
     * */
    public PlayerInventory(Player player) {
        this.player = player;
    }

    /**
     * 设置玩家光标上的物品
     * */
    public void setCursor(Item item){
        PacketSetContainerSlot packetSetContainerSlot = new PacketSetContainerSlot();
        packetSetContainerSlot.setWindowID(-1);
        packetSetContainerSlot.setStateID(0);
        packetSetContainerSlot.setSlotID((short) 0);
        packetSetContainerSlot.setSlot(item.createSlot());
        player.getClientConnection().sendPacket(packetSetContainerSlot);
    }

}

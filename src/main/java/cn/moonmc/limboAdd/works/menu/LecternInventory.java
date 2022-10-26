package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.packets.out.PacketOpenMenu;
import cn.moonmc.limboAdd.works.entity.Player;

/**
 * @author CNLuminous 2022/10/26
 */
public class LecternInventory extends ShowInventory {
    private Item book;

    public LecternInventory(Item book) {
        this.book = book;
    }

    @Override
    protected void show(Player player) {
        PacketOpenMenu packetOpenMenu = new PacketOpenMenu();
        packetOpenMenu.setSlots(0);
        packetOpenMenu.setWindowsType(PacketOpenMenu.WindowsType.lectern);
        packetOpenMenu.setWindowID(windowID);
        player.getClientConnection().sendPacket(packetOpenMenu);
        sendSlotUp(0,book,player);
    }
    protected void sendSlotUp(int slot,Item item, Player player) {
        if (item!=null){
            super.sendSlot(slot, item, player);
        }
    }
}

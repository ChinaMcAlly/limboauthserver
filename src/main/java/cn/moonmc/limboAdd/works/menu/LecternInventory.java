package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.packets.out.PacketOpenMenu;
import cn.moonmc.limboAdd.packets.out.PacketSetContainerProperty;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.Lister;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerClickButtonContainer;
import lombok.Getter;

/**
 * @author CNLuminous 2022/10/26
 */
public class LecternInventory extends ShowInventory {
    private Item book;

    @Getter
    private int nowId;

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

        setClickButtonLister(event -> {
            if (event.getButtonId()==1){
                nowId -= 1;
            }else if (event.getButtonId()==2){
                nowId += 1;
            }
            changePage(event.getPlayer(), (short) nowId);
        });
    }
    protected void sendSlotUp(int slot,Item item, Player player) {
        if (item!=null){
            super.sendSlot(slot, item, player);
        }
    }

    protected void changePage(Player player,short page){
        PacketSetContainerProperty packetSetContainerProperty = new PacketSetContainerProperty();
        packetSetContainerProperty.setWindowID(windowID);
        packetSetContainerProperty.setProperty((short) 0);
        packetSetContainerProperty.setValue(page);
        player.getClientConnection().sendPacket(packetSetContainerProperty);


    }
}

package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.packets.out.PacketOpenMenu;
import cn.moonmc.limboAdd.packets.out.PacketSetContainerProperty;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.Lister;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerClickButtonContainer;
import lombok.Getter;

import java.util.function.Consumer;

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
        //打开窗口
        PacketOpenMenu packetOpenMenu = new PacketOpenMenu();
        packetOpenMenu.setSlots(0);
        packetOpenMenu.setWindowsType(PacketOpenMenu.WindowsType.lectern);
        packetOpenMenu.setWindowID(windowID);
        player.getClientConnection().sendPacket(packetOpenMenu);
        //设置书
        sendSlotUp(player);
    }

    @Override
    protected void beClickButton(PlayerClickButtonContainer event) {
        if (event.getButtonId()==1){
            nowId -= 1;
        }else if (event.getButtonId()==2){
            nowId += 1;
        }
        changePage();
        super.beClickButton(event);
    }

    protected void sendSlotUp(Player player) {
        if (book!=null){
            super.sendSlot(0, book, player);
        }
    }

    /**
     * 给所有玩家更新页码
     * */
    protected void changePage(){
        PacketSetContainerProperty packetSetContainerProperty = new PacketSetContainerProperty();
        packetSetContainerProperty.setWindowID(windowID);
        packetSetContainerProperty.setProperty((short) 0);
        packetSetContainerProperty.setValue((short) nowId);
        getOpenPlayers().forEachRemaining(player -> player.getClientConnection().sendPacket(packetSetContainerProperty));
    }
}

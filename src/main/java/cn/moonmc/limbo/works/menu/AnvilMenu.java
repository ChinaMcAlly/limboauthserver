package cn.moonmc.limbo.works.menu;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.packets.out.PacketOpenMenu;
import cn.moonmc.limbo.packets.out.PacketSetContainerSlot;
import cn.moonmc.limbo.works.message.JsonText;
import lombok.Getter;

import java.util.Random;

/**
 * 一个只展打开界面的铁砧界面
 * @author jja8
 * */
public class AnvilMenu extends Menu {
    @Getter
    JsonText title;
    int windowID = new Random().nextInt(0,255);

    @Getter
    Item in1;
    @Getter
    Item in2;
    @Getter
    Item out;

    public AnvilMenu(JsonText title) {
        this.title = title;
    }

    public void setIn1(Item in1) {
        this.in1 = in1;
        getOpenPlayers().forEachRemaining(player -> {
            setSlot(0,in1,player);
        });
    }

    public void setIn2(Item in2) {
        this.in2 = in2;
        getOpenPlayers().forEachRemaining(player -> {
            setSlot(1,in2,player);
        });
    }

    public void setOut(Item out) {
        this.out = out;
        getOpenPlayers().forEachRemaining(player -> {
            setSlot(2,in2,player);
        });
    }

    @Override
    protected void show(Player player) {
        PacketOpenMenu packetOpenMenu = new PacketOpenMenu();
        packetOpenMenu.setTitle(title);
        packetOpenMenu.setSlots(0);
        packetOpenMenu.setWindowsType(PacketOpenMenu.WindowsType.anvil);
        packetOpenMenu.setWindowID(windowID);
        player.getClientConnection().sendPacket(packetOpenMenu);
        setSlot(0,in1,player);
        setSlot(1,in2,player);
        setSlot(2,out,player);
    }

    private void setSlot(int slot,Item item,Player player){
        if (item!=null){
            PacketSetContainerSlot packetSetContainerSlot = new PacketSetContainerSlot();
            packetSetContainerSlot.setWindowID(windowID);
            packetSetContainerSlot.setSlotID((short) slot);
            packetSetContainerSlot.setSlot(item.createSlot());
            player.getClientConnection().sendPacket(packetSetContainerSlot);
        }
    }
}

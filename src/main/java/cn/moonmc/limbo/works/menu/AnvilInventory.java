package cn.moonmc.limbo.works.menu;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.packets.out.PacketOpenMenu;
import cn.moonmc.limbo.packets.out.PacketSetContainerProperty;
import cn.moonmc.limbo.packets.out.PacketSetContainerSlot;
import cn.moonmc.limbo.works.event.playerEvent.PlayerClickContainer;
import cn.moonmc.limbo.works.message.JsonText;
import lombok.Getter;

import java.util.Random;

/**
 * 一个铁砧界面，玩家拿走物品或放上物品in1，in2并不会更新哈。因为反正也用不着awa
 * @author jja8
 * */
public class AnvilInventory extends ShowInventory {
    @Getter
    JsonText title;
    int windowID = new Random().nextInt(0,127);

    @Getter
    Item in1;
    @Getter
    Item in2;
    @Getter
    Item out;

    /**
     * 维修成本
     * */
    @Getter
    short repairCost = 0;

    public AnvilInventory(JsonText title) {
        this.title = title;
    }

    public void setIn1(Item in1) {
        this.in1 = in1;
        getOpenPlayers().forEachRemaining(player -> sendSlot(0,in1,player));
    }

    public void setIn2(Item in2) {
        this.in2 = in2;
        getOpenPlayers().forEachRemaining(player -> sendSlot(1,in2,player));
    }

    public void setOut(Item out) {
        this.out = out;
        getOpenPlayers().forEachRemaining(player -> sendSlot(2,in2,player));
    }

    /**
     * 设置维修成本
     * */
    public void setRepairCost(short repairCost) {
        this.repairCost = repairCost;
        getOpenPlayers().forEachRemaining(this::sendRepairCost);
    }

    @Override
    protected void show(Player player) {
        PacketOpenMenu packetOpenMenu = new PacketOpenMenu();
        packetOpenMenu.setTitle(title);
        packetOpenMenu.setSlots(0);
        packetOpenMenu.setWindowsType(PacketOpenMenu.WindowsType.anvil);
        packetOpenMenu.setWindowID(windowID);
        player.getClientConnection().sendPacket(packetOpenMenu);
        sendSlot(0,in1,player);
        sendSlot(1,in2,player);
        sendSlot(2,out,player);
    }

    @Override
    protected void beClick(PlayerClickContainer event) {
        switch (event.getSlot()) {
            case 0, 1 -> sendRepairCost(event.getPlayer());
        }
    }

    /**
     * 发送物品更新
     * */
    private void sendSlot(int slot,Item item,Player player){
        if (item!=null){
            PacketSetContainerSlot packetSetContainerSlot = new PacketSetContainerSlot();
            packetSetContainerSlot.setWindowID(windowID);
            packetSetContainerSlot.setSlotID((short) slot);
            packetSetContainerSlot.setSlot(item.createSlot());
            player.getClientConnection().sendPacket(packetSetContainerSlot);
            //每次更新都需要发送维修成本
            sendRepairCost(player);
        }
    }

    /**
     * 发送维修成本
     * */
    private void sendRepairCost(Player player){
        PacketSetContainerProperty packetSetContainerProperty = new PacketSetContainerProperty();
        packetSetContainerProperty.setWindowID(windowID);
        packetSetContainerProperty.setProperty((short) 0);
        packetSetContainerProperty.setValue(repairCost);
        player.getClientConnection().sendPacket(packetSetContainerProperty);
    }
}

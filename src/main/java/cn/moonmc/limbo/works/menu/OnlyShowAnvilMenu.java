package cn.moonmc.limbo.works.menu;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.packets.out.PacketOpenMenu;
import cn.moonmc.limbo.works.message.JsonText;

import java.util.Random;

/**
 * 一个只展打开界面的铁砧界面
 * @author jja8
 * */
public class OnlyShowAnvilMenu implements Show{
    JsonText title;
    int windowID = new Random().nextInt(0,255);

    public OnlyShowAnvilMenu(JsonText title) {
        this.title = title;
    }

    @Override
    public void show(Player player) {
        PacketOpenMenu packetOpenMenu = new PacketOpenMenu();
        packetOpenMenu.setTitle(title);
        packetOpenMenu.setSlots(0);
        packetOpenMenu.setWindowsType(PacketOpenMenu.WindowsType.anvil);
        packetOpenMenu.setWindowID(windowID);
        player.getClientConnection().sendPacket(packetOpenMenu);
    }

    @Override
    public int windowID() {
        return windowID;
    }
}

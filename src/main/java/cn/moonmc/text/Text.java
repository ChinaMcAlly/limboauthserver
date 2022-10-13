package cn.moonmc.text;

import cn.moonmc.limbo.eventWork.EventManager;
import cn.moonmc.limbo.eventWork.Lister;
import cn.moonmc.limbo.eventWork.event.player.*;
import cn.moonmc.limbo.packets.out.PacketOpenWindow;
import cn.moonmc.limbo.packets.out.PacketSetContainerProperty;
import cn.moonmc.limbo.packets.out.PacketSetContainerSlot;
import com.grack.nanojson.JsonWriter;
import net.kyori.adventure.nbt.CompoundBinaryTag;

import java.util.Map;

/**
 * 一个测试类
 * @author jja8
 * */
public class Text {

    public static void main(){
        EventManager.regLister(new Lister<>(PlayerChatEvent.class) {
            @Override
            public void listen(PlayerChatEvent event) {
                System.out.println(event.getPlayer().getName() + ":" + event.getChat());

                PacketOpenWindow packetOpenWindow = new PacketOpenWindow();
                packetOpenWindow.setWindowsType(PacketOpenWindow.WindowsType.anvil);
                packetOpenWindow.setSlots(3);
                packetOpenWindow.setTitle(event.getChat());

                event.getPlayer().getClientConnection().sendPacket(packetOpenWindow);

                PacketSetContainerSlot pa = new PacketSetContainerSlot();
                pa.setSlotID((short) 0);
                pa.getSlot().setCount(1);
                pa.getSlot().setHasItem(true);
                pa.getSlot().setItemID(829);
                pa.getSlot().setNbt(CompoundBinaryTag
                        .builder()
                        .put(
                                "display",
                                CompoundBinaryTag
                                        .builder()
                                        .putString(
                                                "Name",
                                                JsonWriter.string(
                                                        Map.of("text","我是纸")
                                                )
                                        ).build()
                        ).build());
               event.getPlayer().getClientConnection().sendPacket(pa);


            }
        });
        EventManager.regLister(new Lister<>(PlayerRenameItem.class){
            @Override
            public void listen(PlayerRenameItem event) {
                System.out.println(event.getName());
                event.getPlayer().getClientConnection().sendPacket(new PacketSetContainerProperty());
            }
        });

        EventManager.regLister(new Lister<>(PlayerCommandEvent.class) {
            @Override
            public void listen(PlayerCommandEvent event) {
                System.out.println(event.getPlayer().getName() + "/" + event.getCommand());
            }
        });

        EventManager.regLister(new Lister<>(PlayerJoinEvent.class) {
            @Override
            public void listen(PlayerJoinEvent event) {
                System.out.println(event.getPlayer().getName() + "进入游戏");
            }
        });

        EventManager.regLister(new Lister<>(PlayerQuitEvent.class) {

            @Override
            public void listen(PlayerQuitEvent event) {
                System.out.println(event.getPlayer().getName() + "退出游戏");
            }
        });
    }
}

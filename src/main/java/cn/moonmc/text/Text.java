package cn.moonmc.text;

import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.*;
import cn.moonmc.limbo.packets.out.PacketSetContainerProperty;
import cn.moonmc.limbo.works.menu.AnvilInventory;
import cn.moonmc.limbo.works.menu.Item;
import cn.moonmc.limbo.works.menu.ItemNBTs;
import cn.moonmc.limbo.works.menu.ItemType;
import cn.moonmc.limbo.works.message.JsonText;

import java.util.List;

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

                AnvilInventory anvilMenu = new AnvilInventory(new JsonText(event.getChat()));
                Item item = new Item();
                item.setItemID(ItemType.paper);
                item.setCount(10);
                ItemNBTs itemNBTs = new ItemNBTs();
                itemNBTs.setDisplayName(new JsonText("哈哈哈"));
                itemNBTs.setLore(List.of(new JsonText("aaaaa"),new JsonText("bbbbbb")));
                item.setItemNBTs(itemNBTs);
                anvilMenu.setIn1(item);
                anvilMenu.setIn2(item);
                anvilMenu.setOut(item);
                anvilMenu.setRepairCost((short) 0);
                event.getPlayer().openInventory(anvilMenu);


            }
        });
//        EventManager.regLister(new Lister<>(PlayerRenameItem.class){
//            @Override
//            public void listen(PlayerRenameItem event) {
//                System.out.println(event.getName());
//                event.getPlayer().getClientConnection().sendPacket(new PacketSetContainerProperty());
//            }
//        });
//
//        EventManager.regLister(new Lister<>(PlayerCommandEvent.class) {
//            @Override
//            public void listen(PlayerCommandEvent event) {
//                System.out.println(event.getPlayer().getName() + "/" + event.getCommand());
//            }
//        });
//
//        EventManager.regLister(new Lister<>(PlayerConnectEvent.class) {
//            @Override
//            public void listen(PlayerConnectEvent event) {
//                System.out.println(event.getPlayer().getName() + "进入游戏");
//            }
//        });
//
//        EventManager.regLister(new Lister<>(PlayerQuitEvent.class) {
//
//            @Override
//            public void listen(PlayerQuitEvent event) {
//                System.out.println(event.getPlayer().getName() + "退出游戏");
//            }
//        });
    }
}

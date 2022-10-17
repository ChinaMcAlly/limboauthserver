package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.packets.out.PacketOpenMenu;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.message.JsonText;
import lombok.Getter;

import java.util.Random;

/**
 * 代表一个箱子库存
 * @author jja8
 * */
public class GenericInventory extends ShowInventory{
    int windowID = new Random().nextInt(0,127);
    /**
     * 箱子标题
     * */
    @Getter
    JsonText title;
    /**
     * 箱子大小，（箱子的行数*9）
     * */
    @Getter
    int genericSize;
    /**
     * 箱子的物品数组
     * */
    Item[] itmes;

    public GenericInventory(JsonText title, int genericSize) {
        if (genericSize%9!=0 || genericSize<=0 || genericSize>54){
            throw new Error("箱子的大小必须是9的倍数，并且大于0小于等于54");
        }
        this.title = title;
        this.genericSize = genericSize;
        this.itmes = new Item[genericSize];
    }

    @Override
    protected void show(Player player) {
        PacketOpenMenu packetOpenMenu = new PacketOpenMenu();
        packetOpenMenu.setWindowID(windowID);
        packetOpenMenu.setWindowsTypeID(genericSize/9-1);
        packetOpenMenu.setSlots(genericSize);
        packetOpenMenu.setTitle(title);
        player.getClientConnection().sendPacket(packetOpenMenu);

        //发送物品信息
        for (int i = 0; i < itmes.length; i++) {
            if (itmes[i]!=null){
                sendSlot(i,itmes[i],player);
            }
        }
    }

    /**
     * 设置某格子的物品
     * */
    public void setItem(int slot,Item item){
        if (slot>0&&slot<itmes.length){
            itmes[slot] = item;
            //更新玩家格子
           getOpenPlayers().forEachRemaining(player -> sendSlot(slot,item,player));
        }else {
            throw new Error("物品位置超出容器范围！");
        }
    }

    /**
     * 获取某格子的物品
     * */
    public Item getItem(int slot){
        if (slot>0&&slot<itmes.length){
            return itmes[slot];
        }else {
            throw new Error("物品位置超出容器范围！");
        }
    }

}

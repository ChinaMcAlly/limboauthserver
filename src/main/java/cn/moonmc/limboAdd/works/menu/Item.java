package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.packets.out.PacketSetContainerSlot;
import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;

/**
 * 代表一个物品
 * @author jja8
 * */
@Data
public class Item {
    ItemType itemID;
    int count = 1;
    ItemNBTs itemNBTs;

    public Item setItemID(ItemType itemID) {
        this.itemID = itemID;
        return this;
    }

    public Item setCount(int count) {
        this.count = count;
        return this;
    }

    public Item setItemNBTs(ItemNBTs itemNBTs) {
        this.itemNBTs = itemNBTs;
        return this;
    }

    public PacketSetContainerSlot.Slot createSlot(){
        PacketSetContainerSlot.Slot slot = new PacketSetContainerSlot.Slot();
        if (itemID==null||count<=0){
            slot.setHasItem(false);
            return slot;
        }
        slot.setItemID(itemID.id);
        slot.setCount(count);
        if (itemNBTs==null){
            slot.setNbt(CompoundBinaryTag.builder().build());
        }else {
            slot.setNbt(itemNBTs.createNBT());
        }
        return slot;
    }
}

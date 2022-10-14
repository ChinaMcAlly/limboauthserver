package cn.moonmc.limbo.works.menu;

import cn.moonmc.limbo.packets.out.PacketSetContainerSlot;
import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;

@Data
public class Item {
    ItemType itemID;
    int count = 1;
    ItemNBTs itemNBTs;

    public PacketSetContainerSlot.Slot createSlot(){
        PacketSetContainerSlot.Slot slot = new PacketSetContainerSlot.Slot();
        if (itemID==null||count<=0){
            slot.setHasItem(false);
            return slot;
        }
        slot.setItemID(slot.getItemID());
        slot.setCount(count);
        if (itemNBTs==null){
            slot.setNbt(CompoundBinaryTag.builder().build());
        }else {
            slot.setNbt(itemNBTs.createNBT());
        }
        return slot;
    }
}

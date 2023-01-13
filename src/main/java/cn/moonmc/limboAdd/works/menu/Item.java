package cn.moonmc.limboAdd.works.menu;

import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 代表一个物品
 * @author jja8
 * */
@Data
public class Item {
    ItemType itemID;
    int count = 1;
    ItemNBTs itemNBTs;
    Version version;

    public Item(Version version) {
        this.version = version;
    }

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

    public Slot createSlot(){
        Slot slot = new Slot();
        if (itemID==null||count<=0){
            slot.setHasItem(false);
            return slot;
        }
        slot.setItemType(itemID);
        slot.setCount(count);
        if (itemNBTs==null){
            slot.setNbt(CompoundBinaryTag.builder().build());
        }else {
            slot.setNbt(itemNBTs.getNBT());
        }
        return slot;
    }

    public CompoundBinaryTag ToNBT(){
        CompoundBinaryTag.@NotNull Builder nbt = CompoundBinaryTag.builder();
        nbt.putInt("id",itemID.getItemTypeNetID(version));
        nbt.put("tag", itemNBTs.getNBT());
        return nbt.build();
    }

    public Item copy() {
        return new Item(version).setItemID(itemID).setItemNBTs(itemNBTs.copy()).setCount(count);
    }
}

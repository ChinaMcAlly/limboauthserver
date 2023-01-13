package cn.moonmc.limboAdd.works.menu;

import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;

@Data
public class Slot {
    boolean hasItem = true;
    ItemType itemType = ItemType.stone;
    int count = 1;
    CompoundBinaryTag nbt = CompoundBinaryTag.builder().build();
}

package cn.moonmc.limbo.packets.out;

import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 设置物品栏内容包 https://wiki.vg/Protocol#Set_Container_Slot
 * @author jja8
 * */
@Data
public class PacketSetContainerSlot implements PacketOut {

    @Data
    public static class Slot{
        boolean hasItem = true;
        int itemID = 829;
        int count = 1;
        CompoundBinaryTag nbt = CompoundBinaryTag.builder().build();
    }

    /**
     * 要设置的窗口id  -1设置玩家鼠标 -2设置玩家背包 其他窗口使用PacketOpenWindow的id
     * */
    int windowID = 56;

    /**
     * 设置要更新的格子
     * */
    short slotID = 0;

    /**
     * 目前来说没什么用
     * */
    int stateID = 0;

    /**
     * 设置格子内容
     * */
    Slot slot = new Slot();

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeByte(windowID);//
        msg.writeVarInt(stateID);
        msg.writeShort(slotID);

        //物品
        msg.writeBoolean(slot.hasItem);
        msg.writeVarInt(slot.itemID);
        msg.writeByte(slot.count);
        msg.writeCompoundTag(slot.nbt);
    }
}

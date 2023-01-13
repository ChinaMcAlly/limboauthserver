package cn.moonmc.limboAdd.packets.out;

import cn.moonmc.limboAdd.works.menu.Slot;
import lombok.Data;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 设置物品栏内容包 https://wiki.vg/Protocol#Set_Container_Slot
 * @author jja8
 * */
@Data
public class PacketSetContainerSlot implements PacketOut {

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
        msg.writeByte(windowID);
        msg.writeVarInt(stateID);
        msg.writeShort(slotID);

        //物品
        msg.writeBoolean(slot.isHasItem());
        msg.writeVarInt(slot.getItemType().getItemTypeNetID(version));
        msg.writeByte(slot.getCount());
        msg.writeCompoundTag(slot.getNbt());
    }
}

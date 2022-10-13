package cn.moonmc.limbo.packets.out;

import com.grack.nanojson.JsonWriter;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

import java.util.Map;

/**
 * 设置物品栏内容包 https://wiki.vg/Protocol#Set_Container_Slot
 * @author jja8
 * */
public class PacketSetContainerSlot implements PacketOut {

    public static class Slot{
        boolean hasItem = true;
        int itemID = 829;
        int count = 1;
        byte[] nbt = new byte[]{0};

        public boolean isHasItem() {
            return hasItem;
        }

        public void setHasItem(boolean hasItem) {
            this.hasItem = hasItem;
        }

        public int getItemID() {
            return itemID;
        }

        public void setItemID(int itemID) {
            this.itemID = itemID;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public byte[] getNbt() {
            return nbt;
        }

        public void setNbt(byte[] nbt) {
            this.nbt = nbt;
        }
    }

    int windowID = 56;

    short slotID = 0;

    Slot slot = new Slot();

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeByte(windowID);//
        msg.writeVarInt(0);
        msg.writeShort(0);

        //物品
        msg.writeBoolean(slot.hasItem);
        msg.writeVarInt(slot.itemID);
        msg.writeByte(slot.count);

       // msg.writeBytes(slot.nbt);
        msg.writeCompoundTag(
                CompoundBinaryTag
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
                        ).build()
        );
    }

    public Slot getSlot() {
        return slot;
    }

    /**
     * 设置格子内容
     * */
    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public int getWindowID() {
        return windowID;
    }

    /**
     * 要设置的窗口id  -1设置玩家鼠标 -2设置玩家背包 其他窗口使用PacketOpenWindow的id
     * */
    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

    public short getSlotID() {
        return slotID;
    }

    /**
     * 设置要更新的格子
     * */
    public void setSlotID(short slotID) {
        this.slotID = slotID;
    }
}

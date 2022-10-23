package cn.moonmc.limboAdd.packets.out;

import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 设置玩家指向物品栏的物品包 https://wiki.vg/Protocol#Set_Held_Item_.28serverbound.29
 * @author CNLuminous 2022/10/23
 */
public class PacketSetHeldItem implements PacketOut {

        /**
        * 设置玩家指向物品栏的物品 0-8
        */
        private byte slot;

        public PacketSetHeldItem(byte slot) {
            this.slot = slot;
        }

    public PacketSetHeldItem() {
            this.slot = 0;
    }

    @Override
        public void encode(ByteMessage msg, Version version) {
            msg.writeVarInt(slot);
        }
}

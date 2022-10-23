package cn.moonmc.limboAdd.packets.out;

import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * @author CNLuminous 2022/10/23
 */
public class PacketOpenBook implements PacketOut {
    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(0);
    }
}

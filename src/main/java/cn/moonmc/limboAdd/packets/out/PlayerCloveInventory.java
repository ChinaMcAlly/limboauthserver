package cn.moonmc.limboAdd.packets.out;

import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 关闭用户所打开界面
 * @author CNLuminous 2022/10/19
 */
public class PlayerCloveInventory implements PacketOut {
    int windowId = 0;
    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(windowId);
    }

}

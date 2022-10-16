package cn.moonmc.limbo.packets.in;

import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.playerEvent.PlayerCloseContainer;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketIn;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;

/**
 * 关闭容器包
 *
 * @author jja8
 */
public class PacketCloseContainer implements PacketIn {

    int windowId;

    @Override
    public void decode(ByteMessage msg, Version version) {
        windowId = Byte.toUnsignedInt(msg.readByte());
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        EventManager.call(new PlayerCloseContainer(conn.getPlayer(), windowId));
    }
}

package cn.moonmc.limboAdd.packets.in;

import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerTurnPagesEvent;
import cn.moonmc.limboAdd.works.message.ClickEventChangePage;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketIn;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;

/**
 * @author CNLuminous 2022/10/26
 */
public class PacketPlayerTurnPages implements PacketIn {
    Integer rid;
    @Override
    public void decode(ByteMessage msg, Version version) {
        rid =msg.readVarInt();
        System.out.println(msg);
        System.out.println(msg);
        System.out.println(msg);
//        command = msg.readString();
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        conn.getPlayer().getClientConnection().sendPacket(new ClickEventChangePage(rid+1));
        EventManager.call(new PlayerTurnPagesEvent(conn.getPlayer()));
    }
}

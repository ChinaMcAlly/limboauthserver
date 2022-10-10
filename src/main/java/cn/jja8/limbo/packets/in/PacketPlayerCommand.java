package cn.jja8.limbo.packets.in;

import cn.jja8.limbo.eventWork.EventManager;
import cn.jja8.limbo.eventWork.event.player.PlayerCommandEvent;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketIn;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;

public class PacketPlayerCommand implements PacketIn {
    String command;

    @Override
    public void decode(ByteMessage msg, Version version) {
        command = msg.readString();
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        EventManager.call(new PlayerCommandEvent(conn.getPlayer(),command));
    }
}

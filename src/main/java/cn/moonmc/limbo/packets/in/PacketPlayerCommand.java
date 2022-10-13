package cn.moonmc.limbo.packets.in;

import cn.moonmc.limbo.eventWork.EventManager;
import cn.moonmc.limbo.eventWork.event.player.PlayerCommandEvent;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketIn;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;

/**
 * 接收玩家命令包
 * @author jja8
 * */
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

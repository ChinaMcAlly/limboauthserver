package cn.moonmc.limboAdd.packets.in;

import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerChatEvent;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketIn;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;

/**
 * 接受玩家聊天包
 * @author jja8
 * */
public class PacketPlayerChatMessage implements PacketIn {
    String cat;

    @Override
    public void decode(ByteMessage msg, Version version) {
        cat = msg.readString();
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        EventManager.call(new PlayerChatEvent(conn.getPlayer(),cat));
    }
}

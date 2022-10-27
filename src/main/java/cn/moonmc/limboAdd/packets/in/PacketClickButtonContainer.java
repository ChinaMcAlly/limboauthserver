package cn.moonmc.limboAdd.packets.in;

import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerClickButtonContainer;
import cn.moonmc.limboAdd.works.message.ClickEventChangePage;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketIn;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;

/**
 * @author CNLuminous 2022/10/26
 */
public class PacketClickButtonContainer implements PacketIn {
    Integer windowId;
    Integer buttonId;
    Integer nowPage;
    @Override
    public void decode(ByteMessage msg, Version version) {
        windowId = msg.readVarInt();
        buttonId = msg.readVarInt();

    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        EventManager.call(new PlayerClickButtonContainer(conn.getPlayer(),buttonId));
    }


}

package cn.jja8.limbo.packets.in;

import cn.jja8.limbo.eventWork.EventManager;
import cn.jja8.limbo.eventWork.event.player.PlayerRenameItem;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketIn;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;

/**
 * 物品重命名包，铁砧每次修改物品名称都会发送给服务器
 * */
public class PacketRenameItem  implements PacketIn {
    String name;

    @Override
    public void decode(ByteMessage msg, Version version) {
        name = msg.readString();
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        EventManager.call(new PlayerRenameItem(conn.getPlayer(),name));
    }
}

package cn.moonmc.limbo.packets.in;

import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.playerEvent.PlayerClickContainer;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketIn;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;

/**
 * 玩家点击物品栏包 https://wiki.vg/Protocol#Click_Container_Button
 * */
public class PacketClickContainer implements PacketIn {
    /**
     * windowID
     * */
    int windowID;
    /**
     * 目前来说没什么用
     * */
    int stateID;

    /**
     * 被点击的格子
     * */
    short slot;

    /**
     * 点击的模式
     * */
    int mode;
    /**
     * 点击的按键
     * */
    int button;



    @Override
    public void decode(ByteMessage msg, Version version) {
        windowID = Byte.toUnsignedInt(msg.readByte());
        stateID= msg.readVarInt();
        slot = msg.readShort();
        mode = msg.readVarInt();
        button = msg.readVarInt();

        //还有数据懒得读了，反正也没用
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        EventManager.call(new PlayerClickContainer(conn.getPlayer(),slot,windowID));
    }
}

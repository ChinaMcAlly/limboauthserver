package cn.moonmc.limbo.packets.out;

import cn.moonmc.limbo.works.message.JsonText;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 玩家在游戏中踢出服务器数据包
 *
 * @author CNLuminous
 */
public class PlayDisconnect implements PacketOut {
    private final JsonText reason;

    public PlayDisconnect(JsonText reason) {
        this.reason = reason;
    }

    public PlayDisconnect() {
        reason = new JsonText("null");
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeString(reason.toJsonText());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}

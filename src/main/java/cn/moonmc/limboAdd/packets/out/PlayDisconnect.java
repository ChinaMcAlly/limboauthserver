package cn.moonmc.limboAdd.packets.out;

import cn.moonmc.limboAdd.works.message.JsonText;
import cn.moonmc.limboAdd.works.message.JsonTextParagraph;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 玩家在游戏中踢出服务器数据包
 * @author CNLuminous
 */
public class PlayDisconnect implements PacketOut {
    private final JsonText reason;
    public PlayDisconnect(JsonText reason) {
        this.reason = reason;
    }
    public PlayDisconnect(){
        reason = new JsonTextParagraph("null");
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

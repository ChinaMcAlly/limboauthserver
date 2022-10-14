package ru.nanit.limbo.protocol.packets.play;

import com.grack.nanojson.JsonWriter;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

import java.util.Map;

/**
 * 玩家在游戏中踢出服务器数据包
 * @author CNLuminous
 */
public class PlayDisconnect implements PacketOut {
    private final String reason;
    public PlayDisconnect(String reason) {
        this.reason = reason;
    }
    public PlayDisconnect(){
        reason = null;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeString(JsonWriter.string(Map.of("text",reason)));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}

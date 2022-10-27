package cn.moonmc.limboAdd.packets.out;

import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 插件消息包
 * */
public class PacketPluginMessage implements PacketOut {

    /**
     * 数据包标示
     * */
    @Getter @Setter
    String identifier;

    /**
     * 数据
     * */
    @Getter @Setter
    byte[] bytes;


    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeString(identifier);
        msg.writeBytes(Unpooled.wrappedBuffer(bytes));
    }
}

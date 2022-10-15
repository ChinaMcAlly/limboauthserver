package cn.moonmc.limbo.packets.out;

import lombok.Data;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 更新属性包 https://wiki.vg/Protocol#Set_Container_Property
 * @author jja8
 * */
@Data
public class PacketSetContainerProperty  implements PacketOut {

    /**
     * 要设置的窗口id  -1设置玩家鼠标 -2设置玩家背包 其他窗口使用PacketOpenWindow的id
     * */
    int windowID = 56;

    /**
     * 设置项目
     * */
    short property = 0;

    /**
     * 设置值
     * */
    short value = 0;


    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeByte(windowID);
        msg.writeShort(property);
        msg.writeShort(value);

    }
}

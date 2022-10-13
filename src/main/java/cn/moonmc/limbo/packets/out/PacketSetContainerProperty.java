package cn.moonmc.limbo.packets.out;

import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 更新属性包 https://wiki.vg/Protocol#Set_Container_Property
 * @author jja8
 * */
public class PacketSetContainerProperty  implements PacketOut {

    int windowID = 56;

    short property = 0;

    short value = 0;


    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeByte(windowID);
        msg.writeShort(property);
        msg.writeShort(value);

    }

    public int getWindowID() {
        return windowID;
    }

    /**
     * 要设置的窗口id  -1设置玩家鼠标 -2设置玩家背包 其他窗口使用PacketOpenWindow的id
     * */
    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

    public short getProperty() {
        return property;
    }


    /**
     * 设置项目
     * */
    public void setProperty(short property) {
        this.property = property;
    }

    public short getValue() {
        return value;
    }

    /**
     * 设置值
     * */
    public void setValue(short value) {
        this.value = value;
    }
}

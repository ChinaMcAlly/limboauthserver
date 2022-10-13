package cn.moonmc.jja8.limbo.packets;


import cn.moonmc.jja8.limbo.packets.in.PacketPlayerChatMessage;
import cn.moonmc.jja8.limbo.packets.in.PacketPlayerCommand;
import cn.moonmc.jja8.limbo.packets.in.PacketRenameItem;
import cn.moonmc.jja8.limbo.packets.out.PacketOpenWindow;
import cn.moonmc.jja8.limbo.packets.out.PacketSetContainerProperty;
import cn.moonmc.jja8.limbo.packets.out.PacketSetContainerSlot;
import ru.nanit.limbo.protocol.registry.State;

import static ru.nanit.limbo.protocol.registry.Version.V1_19_1;

public class PackReg {
    public static void reg(State.ProtocolMappings serverBound, State.ProtocolMappings clientBound) {
        //聊天消息包
        serverBound.register(PacketPlayerChatMessage::new,
                new State.Mapping(0x05,V1_19_1,V1_19_1));
        //玩家命令包
        serverBound.register(PacketPlayerCommand::new,
                new State.Mapping(0x04,V1_19_1,V1_19_1));
        //打开窗口包
        clientBound.register(PacketOpenWindow::new,
                new State.Mapping(0x2D,V1_19_1,V1_19_1));
        clientBound.register(PacketSetContainerSlot::new,
                new State.Mapping(0x13,V1_19_1,V1_19_1));
        clientBound.register(PacketSetContainerProperty::new,
                new State.Mapping(0x12,V1_19_1,V1_19_1));

        serverBound.register(PacketRenameItem::new,
                new State.Mapping(0x23,V1_19_1,V1_19_1));
    }
}

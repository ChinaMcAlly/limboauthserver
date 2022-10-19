package cn.moonmc.limboAdd.packets;


import cn.moonmc.limboAdd.packets.in.*;
import cn.moonmc.limboAdd.packets.out.*;
import ru.nanit.limbo.protocol.registry.State;
import ru.nanit.limbo.protocol.registry.Version;

import static ru.nanit.limbo.protocol.registry.Version.V1_19_1;

/**
 * 包注册类
 * @author jja8 CNLuminous
 * */
public class PackReg {
    public static void reg(State.ProtocolMappings serverBound, State.ProtocolMappings clientBound) {
        //聊天消息包
        serverBound.register(PacketPlayerChatMessage::new,
                new State.Mapping(0x05,V1_19_1,V1_19_1));
        //玩家命令包
        serverBound.register(PacketPlayerCommand::new,
                new State.Mapping(0x04,V1_19_1,V1_19_1));
        //打开窗口包
        clientBound.register(PacketOpenMenu::new,
                new State.Mapping(0x2D,V1_19_1,V1_19_1));
        clientBound.register(PacketSetContainerSlot::new,
                new State.Mapping(0x13,V1_19_1,V1_19_1));
        clientBound.register(PacketSetContainerProperty::new,
                new State.Mapping(0x12,V1_19_1,V1_19_1));

        serverBound.register(PacketRenameItem::new,
                new State.Mapping(0x23,V1_19_1,V1_19_1));
        clientBound.register(PlayDisconnect::new,
                new State.Mapping(0x19, Version.getMin(), Version.getMax())
        );
        //关闭window包
        clientBound.register(PlayerCloveInventory::new,
                new State.Mapping(0x10,Version.getMin(),Version.getMax())
        );

        serverBound.register(PacketCloseContainer::new,
                new State.Mapping(0xC,Version.getMin(),Version.getMax()));
        serverBound.register(PacketClickContainer::new,
                new State.Mapping(0xB,Version.getMin(),Version.getMax()));
    }
}

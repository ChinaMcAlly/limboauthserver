package cn.moonmc.limboAdd.packets;


import cn.moonmc.limboAdd.packets.in.*;
import cn.moonmc.limboAdd.packets.out.*;
import ru.nanit.limbo.protocol.registry.State;
import ru.nanit.limbo.protocol.registry.Version;

import static ru.nanit.limbo.protocol.registry.Version.*;

/**
 * 包注册类
 * @author jja8 CNLuminous
 * */
public class PackReg {
    public static void reg(State.ProtocolMappings serverBound, State.ProtocolMappings clientBound) {
        //聊天消息包
        serverBound.register(PacketPlayerChatMessage::new,
                new State.Mapping(0x05,V1_19,V1_19_1),
                new State.Mapping(0x03,V1_18,V1_18_2)
                );
        //玩家命令包 1.19+
        serverBound.register(PacketPlayerCommand::new,
                new State.Mapping(0x04,V1_19,V1_19_1));

        serverBound.register(PacketClickButtonContainer::new,
                new State.Mapping(0x0A,V1_19,V1_19_1),
                new State.Mapping(0x07,V1_18,V1_18_2)
        );
        //打开窗口包
        clientBound.register(PacketOpenMenu::new,
                new State.Mapping(0x2D,V1_19,V1_19_1),
                new State.Mapping(0x2E,V1_18,V1_18_2));
        clientBound.register(PacketSetContainerSlot::new,
                new State.Mapping(0x13,V1_19,V1_19_1),
                new State.Mapping(0x16,V1_18,V1_18_2));
        clientBound.register(PacketSetContainerProperty::new,
                new State.Mapping(0x12,V1_19,V1_19_1),
                new State.Mapping(0x15,V1_18,V1_18_2));

        serverBound.register(PacketRenameItem::new,
                new State.Mapping(0x23,V1_19,V1_19_1),
                new State.Mapping(0x20,V1_18,V1_18_2));
        clientBound.register(PlayDisconnect::new,
                new State.Mapping(0x19,V1_19,V1_19_1),
                new State.Mapping(0x1A,V1_18,V1_18_2)
        );
        //关闭window包
        clientBound.register(PlayerCloveInventory::new,
                new State.Mapping(0x10,V1_19,V1_19_1),
                new State.Mapping(0x13,V1_18,V1_18_2)
        );
        //打开主手上的书
        clientBound.register(PacketOpenBook::new,
                new State.Mapping(0x2C,V1_19,V1_19_1),
                new State.Mapping(0x2D,V1_18,V1_18_2)
        );
        //设置玩家物品栏指向
        clientBound.register(PacketSetHeldItem::new,
                new State.Mapping(0x4A,V1_19,V1_19_1),
                new State.Mapping(0x48,V1_18,V1_18_2)
        );
        //客户端关闭容器
        serverBound.register(PacketCloseContainer::new,
                new State.Mapping(0x0C,V1_19,V1_19_1),
                new State.Mapping(0x09,V1_18,V1_18_2));

        serverBound.register(PacketClickContainer::new,
                new State.Mapping(0x0B,V1_19,V1_19_1),
                new State.Mapping(0x08,V1_18,V1_18_2));


        clientBound.register(PacketPluginMessage::new,
                new State.Mapping(0x16,V1_19,V1_19_1),
                new State.Mapping(0x18,V1_18,V1_18_2)
        );
    }
}

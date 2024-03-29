package cn.moonmc.limboAdd.packets.out;

import cn.moonmc.limboAdd.works.message.JsonText;
import cn.moonmc.limboAdd.works.message.JsonTextParagraph;
import lombok.Data;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

/**
 * 打开界面包 https://wiki.vg/Protocol#Open_Screen
 * @author jja8
 * */
@Data
public class PacketOpenMenu implements PacketOut {
    /**
     * 每个windowID都不能相同，如果相同会认为是同一个window
     * */
    int windowID = 56;

    /**
     * window类型
     * */
    int windowsTypeID = WindowsType.generic_9x3.ID;

    /**
     * window标题
     * */
    JsonText title = new JsonTextParagraph("null");

    /**
     * 格子数量
     * */
    int slots = 0; //格子数量

    public void setWindowsType(WindowsType windowsType){
        windowsTypeID=windowsType.ID;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(windowID);
        msg.writeVarInt(windowsTypeID);
        msg.writeString(title.toJsonText(version));
    }


    public enum WindowsType{
        //参考 https://wiki.vg/Inventory

        generic_9x1(0),
        generic_9x2(1),
        generic_9x3(2),
        generic_9x4(3),
        generic_9x5(4),
        generic_9x6(5),
        generic_3x3(6),
        anvil(7),
        beacon(8),
        blast_furnace(9),
        brewing_stand(10),
        crafting(11),
        enchantment(12),
        furnace(13),
        grindstone(14),
        hopper(15),
        lectern(16),
        loom(17),
        merchant(18),
        shulker_box(19),
        smithing(20),
        smoker(21),
        cartography(22),
        stonecutter(23);
        final int ID;
        WindowsType(int s) {
            this.ID=s;
        }
    }
}

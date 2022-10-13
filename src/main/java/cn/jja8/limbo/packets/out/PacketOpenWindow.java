package cn.jja8.limbo.packets.out;

import com.grack.nanojson.JsonWriter;
import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.PacketOut;
import ru.nanit.limbo.protocol.registry.Version;

import java.util.Map;

/**
 * 打开界面包
 * */
public class PacketOpenWindow implements PacketOut {
    int windowID = 56;

    WindowsType windowsType = WindowsType.generic_9x3;
    String title = "null";

    int slots = 0; //格子数量

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(windowID);
        msg.writeVarInt(windowsType.ID);
        msg.writeString(JsonWriter.string(Map.of("text",title)));
    }

    public WindowsType getWindowsType() {
        return windowsType;
    }

    public void setWindowsType(WindowsType windowsType) {
        this.windowsType = windowsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getWindowID() {
        return windowID;
    }

    public void setWindowID(int windowID) {
        this.windowID = windowID;
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

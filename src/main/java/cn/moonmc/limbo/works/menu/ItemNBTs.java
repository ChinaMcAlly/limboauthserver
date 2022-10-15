package cn.moonmc.limbo.works.menu;

import cn.moonmc.limbo.works.message.JsonText;
import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 先就只搞个名称，到时候再加附魔啥的
 * @author jja8
 * */
@Data
public class ItemNBTs {
    JsonText displayName;
    List<JsonText> lore;

    public CompoundBinaryTag createNBT() {
        CompoundBinaryTag.@NotNull Builder nbt = CompoundBinaryTag.builder();
        addDisplay(nbt);
        return nbt.build();
    }

    /**
     * 显示内容
     */
    protected void addDisplay(CompoundBinaryTag.Builder builder){
        CompoundBinaryTag.@NotNull Builder display = CompoundBinaryTag.builder();
        addDisplayName(display);
        addLore(display);
        builder.put("display", display.build());
    }

    private void addDisplayName(CompoundBinaryTag.Builder display){
        if (displayName==null){
            return;
        }
        display.putString(
                "Name",
                displayName.toJsonText()
        );
    }

    private void addLore(CompoundBinaryTag.Builder display){
        if (lore==null){
            return;
        }
        ListBinaryTag.Builder<net.kyori.adventure.nbt.BinaryTag> binaryTagBuilder = ListBinaryTag.builder();
        for (JsonText jsonText : lore) {
            binaryTagBuilder.add(StringBinaryTag.of(jsonText.toJsonText()));
        }
        display.put("Lore",binaryTagBuilder.build());
    }
}

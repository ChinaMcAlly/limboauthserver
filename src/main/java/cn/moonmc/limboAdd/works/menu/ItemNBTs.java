package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.works.message.JsonText;
import cn.moonmc.limboAdd.works.message.JsonTextParagraph;
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

    /**
     * 设置物品显示名称
     * @return 返回自己，用于神奇写法
     * */
    public ItemNBTs setDisplayName(JsonText displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * 设置物品lore
     * @return 返回自己，用于神奇写法
     * */
    public ItemNBTs setLore(List<JsonText> lore) {
        this.lore = lore;
        return this;
    }

    public CompoundBinaryTag getNBT() {
        CompoundBinaryTag.@NotNull Builder nbt = CompoundBinaryTag.builder();
        builderNBT(nbt);
        return nbt.build();
    }

    /**
     * 子类可重写此方法添加更多属性
     * */
    protected void builderNBT(CompoundBinaryTag.@NotNull Builder nbt){
        addDisplay(nbt);
    }


    private void addDisplay(CompoundBinaryTag.Builder builder){
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

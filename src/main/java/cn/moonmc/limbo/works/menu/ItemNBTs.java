package cn.moonmc.limbo.works.menu;

import cn.moonmc.limbo.works.message.JsonText;
import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;

/**
 * 先就只搞个名称，到时候再加附魔啥的
 * @author jja8
 * */
@Data
public class ItemNBTs {
    JsonText displayName;

    public CompoundBinaryTag createNBT() {
        return CompoundBinaryTag.builder()
                .put(
                        "display",
                        CompoundBinaryTag.builder()
                                .putString(
                                        "Name",
                                        displayName.toJsonText()
                                )
                                .build()
                )
                .build();
    }
}

package cn.moonmc.limboAdd.works.message;

import cn.moonmc.limboAdd.works.menu.Item;
import net.kyori.adventure.nbt.TagStringIO;
import ru.nanit.limbo.protocol.registry.Version;

import java.io.IOException;
import java.util.Map;

/**
 * 鼠标停留时显示物品属性，这个先不要用，我生成的json是没问题的但是就是显示不出来文本到时候再修吧
 * @author jja8
 */
@Deprecated
public class HoverEventShowItem implements HoverEvent {
    final Item item;

    public HoverEventShowItem(Item item) {
        this.item = item;
    }

    @Override
    public Map<String, Object> toMap(Version version) {
        try {
            return Map.of("action", "show_item", "value", TagStringIO.get().asString(item.ToNBT()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

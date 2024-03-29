package cn.moonmc.limboAdd.works.message;

import ru.nanit.limbo.protocol.registry.Version;

import java.util.Map;

/**
 * 鼠标停留时显示文本动作
 * @author jja8
 */
public class HoverEventShowText implements HoverEvent {
    final String text;

    public HoverEventShowText(String text) {
        this.text = text;
    }

    @Override
    public Map<String, Object> toMap(Version version) {
        return Map.of("action", "show_text", "value", text);
    }
}

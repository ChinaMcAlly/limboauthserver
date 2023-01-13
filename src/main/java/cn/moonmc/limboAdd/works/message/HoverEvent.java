package cn.moonmc.limboAdd.works.message;

import ru.nanit.limbo.protocol.registry.Version;

import java.util.Map;

/**
 * 代表一个鼠标停留时动作
 *
 * @author jja8
 */
public interface HoverEvent {
    Map<String, Object> toMap(Version version);
}

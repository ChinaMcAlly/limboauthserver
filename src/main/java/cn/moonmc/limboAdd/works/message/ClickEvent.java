package cn.moonmc.limboAdd.works.message;

import java.util.Map;

/**
 * 代表鼠点击时动作
 * */
public interface ClickEvent {
    Map<String, Object> toMap();
}

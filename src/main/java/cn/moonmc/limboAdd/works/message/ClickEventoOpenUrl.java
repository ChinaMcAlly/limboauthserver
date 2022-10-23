package cn.moonmc.limboAdd.works.message;

import java.util.Map;

/**
 * 鼠标点击执行打开链接
 * */
public class ClickEventoOpenUrl implements ClickEvent{
    String url;

    public ClickEventoOpenUrl(String url) {
        this.url = url;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("action","open_url","value", url);
    }
}

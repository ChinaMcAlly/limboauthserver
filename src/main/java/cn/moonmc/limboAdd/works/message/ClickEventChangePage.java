package cn.moonmc.limboAdd.works.message;

import java.util.Map;

/**
 * 鼠标点击执给书本翻页
 * */
public class ClickEventChangePage implements ClickEvent{
    int page;

    public ClickEventChangePage(int page) {
        this.page = page;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("action","change_page","value", String.valueOf(page));
    }
}

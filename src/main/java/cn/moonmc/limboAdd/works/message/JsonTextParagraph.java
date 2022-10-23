package cn.moonmc.limboAdd.works.message;

import com.grack.nanojson.JsonWriter;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 代表一个段落，可以单独使用也可以添加到文章中
 * @author jja8
 * */
public class JsonTextParagraph implements JsonText{
    @Getter
    final String text;

    /**
     * 鼠标停留时动作
     * */
    @Getter
    HoverEvent hoverEvent;

    /**
     * 鼠标点击时动作
     * */
    @Getter
    ClickEvent clickEvent;

    public JsonTextParagraph(String text) {
        this.text = text;
    }

    public String toJsonText(){
        return JsonWriter.string(toMap());
    }

    /**
     * 鼠标停留时动作
     * */
    public JsonTextParagraph setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        return this;
    }

    /**
     * 鼠标点击时动作
     * */
    public JsonTextParagraph setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("text",text);
        if (hoverEvent!=null){
            map.put("hoverEvent",hoverEvent.toMap());
        }
        if (clickEvent!=null){
            map.put("clickEvent",clickEvent.toMap());
        }
       return map;
    }


}

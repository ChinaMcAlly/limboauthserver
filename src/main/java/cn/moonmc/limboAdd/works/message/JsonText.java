package cn.moonmc.limboAdd.works.message;

import com.grack.nanojson.JsonWriter;
import lombok.Getter;

import java.util.Map;

/**
 * 一个Json文本处理类
 * @author jja8
 * */
public class JsonText {
    @Getter
    String text;

    public JsonText(String text) {
        this.text = text;
    }

    public String toJsonText(){
        return JsonWriter.string(Map.of("text",text));
    }
}

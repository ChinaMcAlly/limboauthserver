package cn.moonmc.limboAdd.works.message;

import com.grack.nanojson.JsonWriter;
import ru.nanit.limbo.protocol.registry.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 代表一篇文章，由许多段落组成
 * @author jja
 * */
public class JsonTextArticle  implements JsonText{

    List<JsonTextParagraph> paragraphs = new ArrayList<>();

    public JsonTextArticle(JsonTextParagraph jsonTextArticle) {
        paragraphs.add(jsonTextArticle);
    }

    /**
     * 添加一段
     * */
    public JsonTextArticle addParagraph(JsonTextParagraph jsonTextParagraph){
        paragraphs.add(jsonTextParagraph);
        return this;
    }

    @Override
    public String toJsonText(Version version) {
        List<Map<String,Object>> list = new ArrayList<>();
        for (JsonTextParagraph paragraph : paragraphs) {
            list.add(paragraph.toMap(version));
        }
        return JsonWriter.string(list);
    }
}

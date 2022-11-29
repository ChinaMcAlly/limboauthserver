package cn.moonmc.ability.notify;

import cn.moonmc.ability.login.data.bookpage.Paragraph;
import cn.moonmc.ability.login.lister.LoginState;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.message.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CNLuminous 2022/11/28
 */
public class ServerEula {
    @Getter
    String eula;

    public ServerEula() {
        try {
            eula = serverEula();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String serverEula() throws IOException {
        String name = "ServerEula.json";
        Path filePath = Paths.get("./", name);

        if (!Files.exists(filePath)) {
            InputStream stream = getClass().getResourceAsStream( "/" + name);

            if (stream == null) {
                throw new FileNotFoundException("Cannot find settings resource file");
            }

            Files.copy(stream, filePath);
        }

        BufferedReader bufferedReader = Files.newBufferedReader(filePath);
        StringBuilder stringBuffer = new StringBuilder();
        String i;
        while ((i=bufferedReader.readLine())!=null){
            stringBuffer.append(i);
        }

        return stringBuffer.toString();
    }
    public static List<JsonText> buildEula(String eula, Player player){
        Gson gson = new Gson();
        List<JsonText> result = new ArrayList<>();
        eula = eula.replace("{quit_command}","/"+player.getAttachments().get(LoginState.class).getQuitCmd());
        eula = eula.replace("{reg_command}","/"+player.getAttachments().get(LoginState.class).getRegCmd());
        JsonArray jsonArray = gson.fromJson(eula, JsonArray.class);
        for (JsonElement json : jsonArray){
            JsonTextArticle jsonTextArticle = new JsonTextArticle(new JsonTextParagraph(""));
            JsonObject object = (JsonObject) json;
            JsonArray array = (JsonArray) object.get("paragraphs");
            for (Object obj: array){
                Paragraph paragraph = gson.fromJson(String.valueOf(obj),Paragraph.class);
                JsonTextParagraph jsonTextParagraph = new JsonTextParagraph(paragraph.getText());
                if (paragraph.getHoverEvent()!=null){
                    jsonTextParagraph.setHoverEvent(new HoverEventShowText(paragraph.getHoverEvent().getText()));
                }
                if (paragraph.getClickEvent()!=null){
                    if (paragraph.getClickEvent().getCommand()!=null){
                        jsonTextParagraph.setClickEvent(new ClickEventRunCommand(paragraph.getClickEvent().getCommand()));
                    }
                    if (paragraph.getClickEvent().getUrl()!=null){
                        jsonTextParagraph.setClickEvent(new ClickEventoOpenUrl(paragraph.getClickEvent().getUrl()));
                    }
                    if (paragraph.getClickEvent().getPage()!=null){
                        jsonTextParagraph.setClickEvent(new ClickEventChangePage(paragraph.getClickEvent().getPage()));
                    }
                }
                jsonTextArticle.addParagraph(jsonTextParagraph);
            }
            result.add(jsonTextArticle);
        }

        return result;
    }
}

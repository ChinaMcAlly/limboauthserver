package cn.moonmc.ability.chooseServer;

import cn.moonmc.ability.AbilityServer;
import cn.moonmc.ability.login.event.LoginSuccessfulEvent;
import cn.moonmc.ability.login.lister.LoginState;
import cn.moonmc.limboAdd.packets.out.PacketPluginMessage;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerCommandEvent;
import cn.moonmc.limboAdd.works.menu.BookItemNBTs;
import cn.moonmc.limboAdd.works.menu.Item;
import cn.moonmc.limboAdd.works.menu.ItemType;
import cn.moonmc.limboAdd.works.menu.LecternInventory;
import cn.moonmc.limboAdd.works.message.JsonText;
import cn.moonmc.limboAdd.works.message.JsonTextParagraph;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import ru.nanit.limbo.protocol.registry.Version;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ChooseServer {
    String jsonText;
    @Getter
    private final AbilityServer server;
    public ChooseServer(AbilityServer server) {
        this.server = server;
        try {
            jsonText = chooseServerBook();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /**
         * 监听玩家登录成功事件
         * */
        EventManager.regLister(LoginSuccessfulEvent.class, event -> {
            Version version = event.getPlayer().getClientConnection().getClientVersion();
            LecternInventory lecternInventory = new LecternInventory(
                    new Item(event.getPlayer().getClientConnection().getClientVersion())
                            .setItemID(ItemType.written_book)
                            .setItemNBTs(
                                    new BookItemNBTs(version)
                                            .setBookTitle("选择服务器")
                                            .setBookAuthor("沙盒事件视角")
                                            .setBookPages(
                                                    List.of(versiona -> jsonText)
                                            )
                            )
            );
            lecternInventory.setCloseLister(event1 -> event1.getPlayer().openInventory(lecternInventory));
            lecternInventory.setClickButtonLister(event1 -> {
                if (event1.getButtonId()==3){
                    event1.getPlayer().disconnect(new JsonTextParagraph("§6§l玩家自主退出服务器"));
                }
            });
            event.getPlayer().openInventory(lecternInventory);

        });

        /**
         * 监听玩家命令事件
         * */
        EventManager.regLister(PlayerCommandEvent.class,event -> {
            LoginState loginState = event.getPlayer().getAttachments().get(LoginState.class);
            if (loginState==null|| !loginState.isLogined()){
                return;
            }
            String[] co = event.getCommand().split(" ");
            if (co.length<2){
                return;
            }
            if (!co[0].equals("gogo")){
                return;
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(co[1]);
            PacketPluginMessage packet = new PacketPluginMessage();
            packet.setIdentifier("bungeecord:main");
            packet.setBytes(out.toByteArray());
            event.getPlayer().getClientConnection().sendPacket(packet);
        });
    }


    private String chooseServerBook() throws IOException {
        String name = "chooseServerBook.json";
        Path filePath = Paths.get("./", name);

        if (!Files.exists(filePath)) {
            InputStream stream = getClass().getResourceAsStream( "/" + name);

            if (stream == null)
                throw new FileNotFoundException("Cannot find settings resource file");

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
}

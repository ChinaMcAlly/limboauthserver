package cn.jja8.limbo;

import com.grack.nanojson.JsonWriter;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.PacketSnapshot;
import ru.nanit.limbo.protocol.packets.play.*;
import ru.nanit.limbo.server.data.BossBar;

import java.util.Map;
import java.util.UUID;

public class Player {
    private final ClientConnection clientConnection;
    public Player(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    /**
     * 获得玩家名称
     * */
    public String getName(){
        return clientConnection.getUsername();
    }

    /**
     * 获得玩家UUID
     * */
    public UUID getUUID(){
        return clientConnection.getUuid();
    }

    /**
     * 向玩家发送Title
     * */
    public void sendTitle(String title,String subtitle,int in,int stay,int out){
        PacketTitleSetTitle packetTitle = new PacketTitleSetTitle();
        PacketTitleSetSubTitle packetSubtitle = new PacketTitleSetSubTitle();
        PacketTitleTimes packetTimes = new PacketTitleTimes();

        packetTitle.setTitle(JsonWriter.string(Map.of("text",title)));
        packetSubtitle.setSubtitle(JsonWriter.string(Map.of("text",subtitle)));
        packetTimes.setFadeIn(in);
        packetTimes.setStay(stay);
        packetTimes.setFadeOut(out);

        PacketSnapshot theTitle = PacketSnapshot.of(packetTitle);
        PacketSnapshot theSubtitle = PacketSnapshot.of(packetSubtitle);
        PacketSnapshot theTimes = PacketSnapshot.of(packetTimes);

        clientConnection.writePacket(theTitle);
        clientConnection.writePacket(theSubtitle);
        clientConnection.sendPacket(theTimes);
    }

    /**
     * 向玩家发送boos血条
     * */
    public void sendBoosBar(String text, float health, BossBar.Color color, BossBar.Division division,UUID uuid){
        PacketBossBar bossBarPaket = new PacketBossBar();
        BossBar bossBar = new BossBar();
        bossBar.setText(JsonWriter.string(Map.of("text",text)));
        bossBar.setHealth(health);
        bossBar.setColor(color);
        bossBar.setDivision(division);
        bossBarPaket.setBossBar(bossBar);
        bossBarPaket.setUuid(uuid);
        PacketSnapshot bossBarPut = PacketSnapshot.of(bossBarPaket);
        clientConnection.sendPacket(bossBarPut);
    }

    /**
     * 向玩家发送消息
     * */
    public void sendMessage(PacketChatMessage.PositionLegacy type,String message){
        PacketChatMessage joinMessage = new PacketChatMessage();
        joinMessage.setJsonData(JsonWriter.string(Map.of("text",message)));
        joinMessage.setPosition(type);
        joinMessage.setSender(UUID.randomUUID());
        clientConnection.sendPacket(PacketSnapshot.of(joinMessage));
    }

}

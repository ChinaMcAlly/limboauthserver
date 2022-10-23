package cn.moonmc.limboAdd.works.entity;

import cn.moonmc.limboAdd.packets.out.*;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerCloseContainer;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerConnectEvent;
import cn.moonmc.limboAdd.works.menu.*;
import cn.moonmc.limboAdd.works.message.JsonText;
import lombok.Getter;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.protocol.PacketSnapshot;
import ru.nanit.limbo.protocol.packets.play.*;
import ru.nanit.limbo.server.data.BossBar;

import java.util.UUID;

/**
 * 代表一个玩家
 * @author jja8 CNLuminous
 * */
public class Player {
    @Getter
    private final ClientConnection clientConnection;

    /**
     * 获取玩家的物品栏
     * */
    @Getter
    private final PlayerInventory playerInventory = new PlayerInventory(this);
    public Player(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
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
     * @author jja8
     * */
    public void sendTitle(JsonText title, JsonText subtitle, int in, int stay, int out){
        PacketTitleSetTitle packetTitle = new PacketTitleSetTitle();
        PacketTitleSetSubTitle packetSubtitle = new PacketTitleSetSubTitle();
        PacketTitleTimes packetTimes = new PacketTitleTimes();

        packetTitle.setTitle(title.toJsonText());
        packetSubtitle.setSubtitle(subtitle.toJsonText());
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
     * @author jja8
     * */
    public void sendBoosBar(JsonText text, float health, BossBar.Color color, BossBar.Division division,UUID uuid){
        PacketBossBar bossBarPaket = new PacketBossBar();
        BossBar bossBar = new BossBar();
        bossBar.setText(text.toJsonText());
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
     * @author jja8
     * */
    public void sendMessage(PacketChatMessage.PositionLegacy type,JsonText message){
        PacketChatMessage joinMessage = new PacketChatMessage();
        joinMessage.setJsonData(message.toJsonText());
        joinMessage.setPosition(type);
        joinMessage.setSender(UUID.randomUUID());
        clientConnection.sendPacket(PacketSnapshot.of(joinMessage));
    }

    /**
     * 将玩家踢出服务器
     * @param reason 原因
     */
    public void disconnect(JsonText reason){
        clientConnection.sendPacketAndClose(new PlayDisconnect(reason));
    }

    /**
     * 给玩家打开界面
     * */
    public void openInventory(ShowInventory inventory){
        getClientConnection().getServer().getAddServer().getInventoryManager().openInventory(inventory,this);
    }

    /**
     * 关闭玩家打开的界面
     */
    public void closeInventory(){
        getClientConnection().sendPacket(new PlayerCloveInventory());
        EventManager.call(new PlayerCloseContainer(this,0));
    }
    /**
     * 获取玩家正在查看的界面
     * */
    public ShowInventory lookingInventory(){
        try {
            return (ShowInventory) getClientConnection().getServer().getAddServer().getInventoryManager().lookingInventory(this);
        }catch (ClassCastException classCastException){
            return null;
        }
    }
    /**
     * 设置玩家主手所在快捷栏位置
     * */
    public void setShortcutBarSlot(int slot){
        clientConnection.sendPacket(new PacketSetHeldItem((byte) slot));
    }
    /**
     * 给玩家打开一本书
     * */
    public void openBook(Item item){
        PacketSetContainerSlot packetSetContainerSlot = new PacketSetContainerSlot();
        packetSetContainerSlot.setWindowID(-2);
        packetSetContainerSlot.setStateID(0);
        packetSetContainerSlot.setSlotID((short) 36);
        packetSetContainerSlot.setSlot(item.createSlot());
        getClientConnection().sendPacket(packetSetContainerSlot);
        setShortcutBarSlot(0);
        clientConnection.sendPacket(new PacketOpenBook());
        packetSetContainerSlot.setSlot(new Item().setItemID(ItemType.air).createSlot());
        getClientConnection().sendPacket(packetSetContainerSlot);
    }
}
